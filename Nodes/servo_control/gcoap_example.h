/*
 * Copyright (C) 2020 Otto-von-Guericke-Universität Magdeburg
 *
 * This file is subject to the terms and conditions of the GNU Lesser General
 * Public License v2.1. See the file LICENSE in the top level directory for more
 * details.
 */

/**
 * @ingroup     examples
 * @{
 *
 * @file
 * @brief       gcoap example
 *
 * @author      Ken Bannister <kb2ma@runbox.com>
 */

#ifndef GCOAP_EXAMPLE_H
#define GCOAP_EXAMPLE_H

#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "fmt.h"
#include "net/gcoap.h"
#include "net/utils.h"
#include "od.h"

#ifdef __cplusplus
extern "C" {
#endif

extern uint16_t req_count;  /**< Counts requests sent by CLI. */

/**
 * @brief   Registers the CoAP resources exposed in the example app
 *
 * Run this exactly one during startup.
 */
void server_init(void);

#ifdef __cplusplus
}
#endif

#endif /* GCOAP_EXAMPLE_H */
/** @} */
