ROOT_DIR = $(abspath $(dir $(lastword $(MAKEFILE_LIST))))

EXTERNAL_MODULE_DIRS := $(ROOT_DIR)/modules
RIOTBASE ?= $(ROOT_DIR)/RIOT

JSON_OUTPUT = keys.json
UUID_OUTPUT = uuid.txt
PUBKEY_OUTPUT = pubkey.der
PRVKEY_OUTPUT = prvkey.der
CREDENTIALS_HEADER = keys.h
QRCODE = qrcode.png

ifneq (,$(filter generatekeys,$(MAKECMDGOALS)))
$(UUID_OUTPUT):
	python3 -c "import uuid; print(uuid.uuid4())" > $@

$(PUBKEY_OUTPUT): $(PRVKEY_OUTPUT)
	openssl ec -in $< -inform DER -pubout -outform DER -out $@

$(PRVKEY_OUTPUT):
	openssl ecparam -name prime256v1 -genkey -outform der -out $@ 

$(CREDENTIALS_HEADER): $(PRVKEY_OUTPUT)
	xxd -i $< > $@

$(JSON_OUTPUT): $(UUID_OUTPUT) $(PUBKEY_OUTPUT)
	echo {\"serial\":\"`cat $(UUID_OUTPUT)`\",\"pubkey\":\"`base64 $(PUBKEY_OUTPUT)`\",\"name\":\"$(APPLICATION)\"} > $@

$(QRCODE): $(JSON_OUTPUT)
	qrencode -o $@ < $<

.INTERMEDIATE: $(PRVKEY_OUTPUT) $(PUBKEY_OUTPUT)
.PHONY: generatekeys
generatekeys: $(CREDENTIALS_HEADER) $(UUID_HEADER) $(QRCODE)
else
CFLAGS += -DSERIAL_UUID=\"$(shell cat $(UUID_OUTPUT))\"
CFLAGS += -DCONFIG_DTLS_ECC
CFLAGS += -DCONFIG_CORD_EP=\"$(shell cat $(UUID_OUTPUT))\"
CFLAGS += -DTHREAD_STACKSIZE_MAIN=THREAD_STACKSIZE_DEFAULT*3
include $(RIOTBASE)/Makefile.include
endif
