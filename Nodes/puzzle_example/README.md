# Example on how to use the puzzle_coap module

The module takes care of handling the coap API for a given puzzle.
It also registers the puzzle at the RD from the provided URI.

Puzzles are defined with this struct:
```C++
typedef struct {
    bool (*get_solved_handler)(void);   /**< bool handler thats get called to see if a puzzle is solved */
    bool (*get_ready_handler)(void);    /**< bool handler thats get called to see if a puzzle is ready or in maintainance */
    void (*set_ready_handler)(bool maintainance);/**< void handler thats get called to set a puzzle in ready or maintainance mode*/
    const char *resource_dir_uri;   /**< char* URI to a CoRE RD */
    const char *name;               /**< char* name of the puzzle */
} puzzle_t;
```

The member `name` must be set to the puzzles name. 
The handler `get_solved_handler` MUST point to a function that can check if the puzzle is solved and returns either true or false. 
The handler `get_ready_handler` MUST point to a function that can check if the puzzle is ready to be solved and returns either true or false.
The handler `set_ready_handler` MUST point to a function that switches the puzzle into either maintainance- or ready mode, depending on the parameter `maintainance`.
The uri `resource_dir_uri` MUST be set to a correct URI. 
The module will still provide a CoAP endpoint even if the registration is unsuccessfull.

To initialise the module, a pointer to the struct is passed to `puzzle_init(..)`. The passed pointer *MUST* be valid for the lifetime of the application.

After that, the puzzle API e.g. `/node/info` is available, which returns a CBOR encoded status of the puzzle.

If your puzzles state changes you MUST call `puzzle_update()`, this will notify potential observers.