ROOT_DIR = $(abspath $(dir $(lastword $(MAKEFILE_LIST))))

EXTERNAL_MODULE_DIRS := $(ROOT_DIR)/modules
RIOTBASE ?= $(ROOT_DIR)/RIOT

include $(RIOTBASE)/Makefile.include
