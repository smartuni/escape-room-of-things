# name of your application
APPLICATION = load_cell

# If no BOARD is found in the environment, use this default:
BOARD ?= pba-d-01-kw2x

# This has to be the absolute path to the RIOT base directory:
RIOTBASE ?= $(CURDIR)/../RIOT

USEMODULE += periph_gpio
USEMODULE += periph_gpio_irq

# Enable the milliseconds timer.
USEMODULE += ztimer
USEMODULE += ztimer_msec

# add load cell module 
FEATURES_REQUIRED += periph_gpio
USEMODULE += hx711
CFLAGS += "-DHX711_PARAM_SCK=GPIO_PIN(4, 4)"
CFLAGS += "-DHX711_PARAM_DOUT=GPIO_PIN(0, 2)"
#CFLAGS += "-DHX711_PARAM_DOUT=GPIO_PIN(3, 6)"
CFLAGS += "-DHX711_PARAM_DIVIDER=204"

# Comment this out to disable code in RIOT that does safety checking
# which is not needed in a production environment but helps in the
# development process:
DEVELHELP ?= 1

# Change this to 0 show compiler invocation lines by default:
QUIET ?= 1

include $(RIOTBASE)/Makefile.include
