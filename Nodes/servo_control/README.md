# Control a servo motor via CoAP

Program can be used to control a servo via CoAP. To use it build and flash teh applicatiton.

# Find your IPv6 address
```sh
> ifconfig
```

# Control the servo using coap put '1' means max position, '0' means min position
```sh
> coap put 2001:db8::5d0f:7b9d:ae49:3ee6 5683 /servo 1
```

# Get the current position using coap get 
```sh
> coap get 2001:db8::5d0f:7b9d:ae49:3ee6 5683 /servo
```