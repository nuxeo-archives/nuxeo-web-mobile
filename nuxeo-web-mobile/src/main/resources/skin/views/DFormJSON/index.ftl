{
  "action" : "${action}",
  "method" : "${actionType}",
  "elements" :  [
    {
      "name" : "title",
      "caption" : "Title",
      "label" : "Title",
      "type" : "text",
      "value" : "${This.title}",
      "placeholder" : "Type here the title of the document"
    },
    {
      "name" : "description",
      "caption" : "Description",
      "label" : "Description",
      "type" : "text",
      "value" : "${This.document.dublincore.description}",
      "placeholder" : "Type here the description of the document"
    },
    {
      "type" : "submit",
      "value" : "Submit"
    }
  ]
}