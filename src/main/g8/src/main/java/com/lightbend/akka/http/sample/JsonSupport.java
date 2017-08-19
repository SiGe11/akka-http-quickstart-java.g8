trait JsonSupport extends SprayJsonSupport {
  import DefaultJsonProtocol._ // import the default encoders for primitive types (Int, String, Lists etc)

  implicit val userJsonFormat = jsonFormat3(User)
  implicit val usersJsonFormat = jsonFormat1(Users)

  implicit val actionPerformedJsonFormat = jsonFormat1(ActionPerformed)
}
