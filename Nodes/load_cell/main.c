/*
Author: Tristan Imken
 */

#include <stdio.h>

#include "board.h"
#include "ztimer.h"

#include "hx711.h"
#include "hx711_params.h"
#include "periph/gpio.h"

#include "test_utils/expect.h"

#define weight 55 /*wanted weight*/
#define tolerance 0.1f /*tolerance of wanted weight*/

static hx711_t dev;
uint8_t times = 10;

int main(void)
{
    int cnt = 0; /*counter*/

    /*initialize LED*/
    gpio_t led0 = GPIO_PIN(PORT_D, 6);
    gpio_mode_t led0_mode = GPIO_OUT;

    gpio_init(led0, led0_mode);

    /*initialize load cell and reset offset*/
    puts("GPIOs example.");
    puts("HX711 test application\n");
    puts("+------------Initializing------------+");
    hx711_init(&dev, &(hx711_params[0]));
    hx711_power_up(&dev);
    puts("Initialization successful\n\n");

    puts("+--------Starting Measurements--------+");
    int32_t units_before = hx711_get_units(&dev, times);
    printf("units before taring: %"PRId32"\n", units_before);
    hx711_tare(&dev, times);

    int32_t units_after = hx711_get_units(&dev, times);

    expect(units_after <= 1);

    /*endless loop*/
    while (1) {
        int32_t value = hx711_get_value(&dev, times);
        int32_t units = hx711_get_units(&dev, times);

        /*check if measurement is within the borders*/
        if ((units>weight-weight*tolerance) && (units<weight+weight*tolerance)) {
            cnt = cnt+1;
            printf("counter: %d \n",cnt); 
        }
        else {
            cnt = 0;
            gpio_set(led0);
        }

        /*set LED if measurement stays 5 seconds*/
        if (cnt>5) {
            gpio_clear(led0);
        }
        printf("value: %"PRId32" units: %"PRId32"\n", value, units);
    }

    return 0;
}
