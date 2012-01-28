$.dform.addType("hellobutton", function(options) {
    // Return a new button element that has all options that
    // don't have a registered subscriber as attributes
    return $("<button>").dformAttr(options).html("Say hello");
 });

$.dform.subscribe("alert", function(options, type) {
    if(type == "hellobutton")
    {
        this.click(function() {
            alert(options);
        });
    }
 });

// Use it like this
//$("#mydiv").buildForm(
//{
//    "type" : "hellobutton",
//    "alert" : "Hello world!"
//});