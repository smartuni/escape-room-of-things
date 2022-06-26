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
#include "hx711.h"
#include "hx711_params.h"
#include "periph/gpio.h"
#include "test_utils/expect.h"
#include "thread.h"

#include "puzzle_coap.h"

#define weight 422 /*wanted weight*/
#define tolerance 0.2f /*tolerance of wanted weight*/

#define STARTUP_DELAY   (1U)
#define MAIN_QUEUE_SIZE (4)
static msg_t _main_msg_queue[MAIN_QUEUE_SIZE];

char meassure_thread_stack[THREAD_STACKSIZE_MAIN + THREAD_EXTRA_STACKSIZE_PRINTF];

/* forward declarations */
static bool _solved(void);
static bool _ready(void);
static void _set_ready(bool maintainance);

/* Setting up the puzzle */
static const puzzle_t puzzle = {
    .get_solved_handler = _solved,
    .get_ready_handler = _ready,
    .set_ready_handler = _set_ready,
    .name = "Weight puzzle",
    .resource_dir_uri = "coap://[fd00:dead:beef::1]:5555",
};

static bool _is_solved = false;
static bool _is_ready = true;

void *meassure_weight(void *arg)
{
    (void) arg;
    int cnt = 0; /*counter*/
    static hx711_t dev;
    uint8_t times = 5;

    puts("In thread");
    /*initialize LED*/
    gpio_t led0 = GPIO_PIN(PORT_D, 6);
    gpio_mode_t led0_mode = GPIO_OUT;

    gpio_init(led0, led0_mode);

    /*initialize load cell and reset offset*/
    hx711_init(&dev, &(hx711_params[0]));
    hx711_power_up(&dev);
    hx711_tare(&dev, times);
    int32_t units_after = hx711_get_units(&dev, times);

    expect(units_after <= 1);

    puts("Entering while loop");
    /*endless loop*/
    while (1) {
        /* int32_t value = hx711_get_value(&dev, times); */
        int32_t units = hx711_get_units(&dev, times);
        printf("%ld\n", units);
        /*check if measurement is within the borders*/
        if ((units > weight - weight * tolerance) && (units < weight + weight * tolerance)) {
            cnt = cnt + 1; 
        }
        else {
            cnt = 0;
            gpio_set(led0);
            if (_is_solved) {
                _is_solved = false;
                /* Notify the module about the news */
                puzzle_update();
            }
        }

        /*set LED if measurement stays 5 seconds*/
        if (cnt == 5) {
            gpio_clear(led0);
            _is_solved = true;
            /* Notify the module about the news */
            puzzle_update();
        }
    }
    return NULL;
}


int main(void)
{
    /* just wait a sec, otherwise the `make falsh term` users 
     * wouldn't see the first print's and might think the app is hanging */
    xtimer_sleep(STARTUP_DELAY);

    /* Initialise the puzzle coap module */
    puzzle_init(&puzzle);

    puts("Starting thread");
    thread_create(
        meassure_thread_stack,
        sizeof(meassure_thread_stack),
        THREAD_PRIORITY_MAIN + 1,
        THREAD_CREATE_STACKTEST,
        meassure_weight, NULL, "meassure_weight"
    );
    puts("go on to shell");

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
