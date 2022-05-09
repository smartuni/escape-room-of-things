/*
 * Copyright (c) 2015-2017 Ken Bannister. All rights reserved.
 *
 * This file is subject to the terms and conditions of the GNU Lesser
 * General Public License v2.1. See the file LICENSE in the top level
 * directory for more details.
 */

#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "fmt.h"
#include "net/gcoap.h"
#include "net/utils.h"
#include "od.h"

#include "cpu.h"
#include "board.h"
#include "xtimer.h"
#include "periph/pwm.h"
#include "servo.h"

#include "gcoap_example.h"

#include "periph/gpio.h"

#define DEV         PWM_DEV(0)
#define CHANNEL     0

#define SERVO_MIN        (1000U)
#define SERVO_MAX        (2000U)

static ssize_t _riot_board_handler(coap_pkt_t* pdu, uint8_t *buf, size_t len, void *ctx);
static ssize_t servo_handler(coap_pkt_t *pdu, uint8_t *buf, size_t len, void *ctx);

/* CoAP resources. Must be sorted by path (ASCII order). */
static const coap_resource_t _resources[] = {
    /*registration of CoAP resource*/
    { "/servo", COAP_PUT | COAP_GET, servo_handler, NULL },
    { "/riot/board", COAP_GET, _riot_board_handler, NULL }
};

static gcoap_listener_t _listener = {
    _resources,
    ARRAY_SIZE(_resources),
    GCOAP_SOCKET_TYPE_UDP,
    NULL,
    NULL,
    NULL
};

static servo_t servo;
void server_init(void)
{
    gcoap_register_listener(&_listener);
    servo_init(&servo, DEV, CHANNEL, SERVO_MIN, SERVO_MAX); /*initialize servo motor*/
    /*test if servo works after initialisation*/
    servo_set(&servo, 1000U); /*move servo to min position*/
    printf("Move to min position\n");
    xtimer_sleep(3);
    servo_set(&servo, 2000U); /*move servo to max position*/
    printf("Move to max position\n");
    xtimer_sleep(3);
    servo_set(&servo, 1000U);
}	

/* implementation of servo handler */
static ssize_t servo_handler(coap_pkt_t *pdu, uint8_t *buf, size_t len, void *ctx)
{
    (void) ctx; /* argument not used */

    char uri[CONFIG_NANOCOAP_URI_MAX] = { 0 };
    /* get the request path */
    if (coap_get_uri_path(pdu, (uint8_t *)uri) <= 0) {
        /* reply with an error if we could not parse the URI */
        return gcoap_response(pdu, buf, len, COAP_CODE_BAD_REQUEST);
    }

    ssize_t resp_len = 0;
    int servo_status = 0;
    unsigned method = coap_method2flag(coap_get_code_detail(pdu));

    switch (method) {
    case COAP_PUT: /* on PUT, we set the status of the servo based on the payload */
        if (pdu->payload_len) {
            servo_status = atoi((char *)pdu->payload);
        } else {
            return gcoap_response(pdu, buf, len, COAP_CODE_BAD_REQUEST);
        }

        if (!servo_status) {
            servo_set(&servo, 1000U); /*servo to min position on "0"*/
            puts("Servo min");
        } else {
            servo_set(&servo, 2000U); /*servo to max position on "1"*/
            puts("Servo max");
        }
        return gcoap_response(pdu, buf, len, COAP_CODE_CHANGED);

    case COAP_GET: /* on GET, we return the status of the servo in plain text */
        /* initialize the CoAP response */
        gcoap_resp_init(pdu, buf, len, COAP_CODE_CONTENT);

        /* set the content format to plain text */
        coap_opt_add_format(pdu, COAP_FORMAT_TEXT);

        /* finish the options indicating that we will include a payload */
        resp_len = coap_opt_finish(pdu, COAP_OPT_FINISH_PAYLOAD);

        /* based on the status, write the value of the payload to send */
        if (!servo_status) {
            pdu->payload[0] = '1';
        } else {
            pdu->payload[0] = '0';
        }
        resp_len++;
        return resp_len;

    }

    return 0;
}

/*
 * Server callback for /riot/board. Accepts only GET.
 *
 * GET: Returns the name of the board in plain text
 */
static ssize_t _riot_board_handler(coap_pkt_t *pdu, uint8_t *buf, size_t len, void *ctx)
{
    (void)ctx;

    /* initialize a new coap response */
    gcoap_resp_init(pdu, buf, len, COAP_CODE_CONTENT);

    /* first we set all the options */
    /* set the format option to "plain text" */
    coap_opt_add_format(pdu, COAP_FORMAT_TEXT);

    /* finish the options sections */
    /* it is important to keep track of the amount of used bytes (resp_len) */
    size_t resp_len = coap_opt_finish(pdu, COAP_OPT_FINISH_PAYLOAD);

    /* write the RIOT board name in the response buffer */
    if (pdu->payload_len >= strlen(RIOT_BOARD)) {
        memcpy(pdu->payload, RIOT_BOARD, strlen(RIOT_BOARD));
        return resp_len + strlen(RIOT_BOARD);
    }
    else {
        /* in this case we use a simple convenience function to create the
         * response, it only allows to set a payload and a response code. */
        puts("gcoap_cli: msg buffer too small");
        return gcoap_response(pdu, buf, len, COAP_CODE_INTERNAL_SERVER_ERROR);
    }
}