/*
 * Copyright (C) 2022 HAW Hamburg
 *
 * This file is subject to the terms and conditions of the GNU Lesser
 * General Public License v2.1. See the file LICENSE in the top level
 * directory for more details.
 */

/**
 * @{
 *
 * @file
 * @brief       Example on using the puzzle_coap module
 *
 * @author      Bennet Blischke <bennet.blischke@haw-hamburg.de>
 *
 * @}
 */

#include <stdio.h>
#include "shell.h"
#include "xtimer.h"

#include "board.h"
#include "ztimer.h"
#include "periph/gpio.h"

#include "puzzle_coap.h"

#define STARTUP_DELAY   (1U)
#define MAIN_QUEUE_SIZE (4)
static msg_t _main_msg_queue[MAIN_QUEUE_SIZE];

char lego_thread_stack[THREAD_STACKSIZE_MAIN + THREAD_EXTRA_STACKSIZE_PRINTF];

/* forward declarations */
static bool _solved(void);
static bool _ready(void);
static void _set_ready(bool maintainance);

/* Setting up the puzzle */
static const puzzle_t puzzle = {
    .get_solved_handler = _solved,
    .get_ready_handler = _ready,
    .set_ready_handler = _set_ready,
    .name = RIOT_APPLICATION,
    .resource_dir_uri = "coap://[fd00:dead:beef::1]:5683",
    .serial = SERIAL_UUID, /* is provided via the build system! */
};

static bool _is_solved = false;
static bool _is_ready = true;

void *lego_handler(void *arg) {
    (void) arg;
    gpio_t input1 = GPIO_PIN(1, 10);
    gpio_t input2 = GPIO_PIN(1, 11);
    gpio_t input3 = GPIO_PIN(1, 12);
    gpio_t led0 = GPIO_PIN(0, 13);
    gpio_mode_t input_mode = GPIO_IN_PU;
    gpio_mode_t led0_mode = GPIO_OUT;

    puts("Magnet example.");
    gpio_init(led0, led0_mode);
    gpio_set(led0);
    gpio_init(input1, input_mode);
    gpio_init(input2, input_mode);
    gpio_init(input3, input_mode);
    
    while (1){;
        if (gpio_read(input1) == 0 && gpio_read(input2) == 0 && gpio_read(input3) == 0){
            gpio_clear(led0);
            if _is_solved == false {
                _is_solved = true;
                puzzle_update();
            }
            /*puts("All magnets detected");*/}
        else{
            if _is_solved == true {
                _is_solved = false;
                puzzle_update();
            }
            gpio_set(led0);
            /*puts("Not all magnets detected");*/
        }

        ztimer_sleep(ZTIMER_MSEC, 500);
    }
}

int main(void)
{
    /* just wait a sec, otherwise the `make falsh term` users 
     * wouldn't see the first print's and might think the app is hanging */
    xtimer_sleep(STARTUP_DELAY);

    puts("Starting thread");
    thread_create(
        lego_thread_stack,
        sizeof(lego_thread_stack),
        THREAD_PRIORITY_MAIN + 1,
        THREAD_CREATE_STACKTEST,
        lego_handler, NULL, "lego_handler"
    );
    puts("go on to shell");

    /* Initialise the puzzle coap module */
    puzzle_init(&puzzle);

    /* for the thread running the shell */
    msg_init_queue(_main_msg_queue, MAIN_QUEUE_SIZE);
    
    /* start shell */
    puts("All up, running the shell now");
    char line_buf[SHELL_DEFAULT_BUFSIZE];
    shell_run(NULL, line_buf, SHELL_DEFAULT_BUFSIZE);
    return 0;
}

/* dummy shell commande to "solve" this puzzle 
 * a real puzzle would call `puzzle_update()` everytime it gets 
 * solved (or when it is no-longer solved). */
static int _cmd_solve(int argc, char **argv)
{
    (void) argc;
    (void) argv;
    _is_solved = !_is_solved;
    /* Notify the module about the news */
    puzzle_update();
    return 0;
}


/* callback handler for the coap API. Returns if the puzzle is solved */
static bool _solved(void)
{
    return _is_solved;
}

/* callback handler for the coap API. Returns if the puzzle is ready to be solved */
static bool _ready(void)
{
    return _is_ready;
}

/* callback handler for the coap API. Is called to set the puzzle in either maintainance or ready mode */
static void _set_ready(bool maintainance)
{
    if (maintainance) {
        /* We are in maintainance mode now; Open the doors, locks etc.... */
        _is_ready = false;
    } else {
        /* Get back to be ready to play! */
        _is_ready = true;
    }
}

SHELL_COMMAND(puzzle_solve, "Toggle if the puzzle is solved", _cmd_solve);
