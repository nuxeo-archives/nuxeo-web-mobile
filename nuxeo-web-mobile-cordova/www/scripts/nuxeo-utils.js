function handleFileError(evt) {
  switch (evt.code) {
  case FileError.NOT_FOUND_ERR:
    console.error("File not found");
    break;
  case FileError.SECURITY_ERR:
    console.error("Security error");
    break;
  case FileError.ABORT_ERR:
    console.error("Abort error");
    break;
  case FileError.NOT_READABLE_ERR:
    console.error("Not readable file error");
    break;
  case FileError.ENCODING_ERR:
    console.error("Encoding error");
    break;
  case FileError.NO_MODIFICATION_ALLOWED_ERR:
    console.error("Not allowed to modificate");
    break;
  case FileError.INVALID_STATE_ERR:
    console.error("Invalide state error");
    break;
  case FileError.SYNTAX_ERR:
    console.error("Syntax error");
    break;
  case FileError.INVALID_MODIFICATION_ERR:
    console.error("Invalid modification error");
    break;
  case FileError.QUOTA_EXCEEDED_ERR:
    console.error("Quota exeeded error");
    break;
  case FileError.TYPE_MISMATCH_ERR:
    console.error("Type mismatch error");
    break;
  case FileError.PATH_EXISTS_ERR:
    console.error("Path exists error");
    break;
  default:
    console.log("Unknown error");
  }
}