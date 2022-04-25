# name of the application
APPLICATION = example

# If no BOARD is found in the environment, use this default:
BOARD ?= example_board

# This has to be the absolute path to the RIOT base directory:
RIOTBASE ?= ../RIOT

# Comment this out to disable code in RIOT that does safety checking
# which is not needed in a production environment but helps in the
# development process:
DEVELHELP ?= 1

USEMODULE += example_module

# as an example, we use a custom board (based on nrf52840dongle for the puzzle)
EXTERNAL_BOARD_DIRS += $(CURDIR)/boards

# Change this to 0 show compiler invocation lines by default:
QUIET ?= 1

include ../common.mk
