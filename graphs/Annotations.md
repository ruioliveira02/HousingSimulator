classDiagram
direction BT
class API
class Endpoint {
  + regex() String
}
class EndpointProcessor {
  + EndpointProcessor() 
  + init(ProcessingEnvironment) void
  + processClasses(RoundEnvironment) String
  + process(Set~TypeElement~, RoundEnvironment) boolean
  + processMethods(RoundEnvironment) String
  + methodCall(Element) String
  + error(Element, String, Object[]?) void
  + SourceVersion supportedSourceVersion
  + Set~String~ supportedAnnotationTypes
}
class IllegalAnnotationException {
  + IllegalAnnotationException(String) 
}
class Main {
  + Main() 
  + main(String[]?) void
}

