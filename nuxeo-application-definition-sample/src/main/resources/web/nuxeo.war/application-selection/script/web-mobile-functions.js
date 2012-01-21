function getCookie(name) {
  var cookies = document.cookie.split(';');
  var cookieTmp = '';
  var cookieNameTmp = '';
  var cookieValueTmp = '';
  var isCookieFound = false;

  for ( i = 0; i < cookies.length; i++ ) {
    cookieTmp = cookies[i].split('=');

    cookieNameTmp = cookieTmp[0].replace(/^\s+|\s+$/g, '');
    if (cookieNameTmp == name) {
      isCookieFound = true;
      if (cookieTmp.length > 1) {
        cookieValueTmp = unescape(cookieTmp[1].replace(/^\s+|\s+$/g, ''));
      }
      return cookieValueTmp;
      break;
    }
    cookieTmp = null;
    cookieNameTmp = '';
  }
  if (!isCookieFound)
  {
    return null;
  }
}

function setCookie(name, value, expDays, path, domain, secure) {
  // Set cookie with name, value etc provided
  // in function call and date from above
  // Number of days the cookie should persist NB expDays='' or undef. => non-persistent
  if (expDays != null ) {
    var expires = new Date();
    expires.setTime(expires.getTime() + (expDays*24*60*60*1000));
  }
  var curCookie = name + "=" + escape(value) +
    ((expires) ? "; expires=" + expires.toGMTString() : "") +
    ((path) ? "; path=" + path : "") +
    ((domain) ? "; domain=" + domain : "") +
    ((secure) ? "; secure" : "");
    document.cookie = curCookie;
}

function getURLParam(strParamName){
  var strReturn = "";
  var strHref = window.location.href;
  if ( strHref.indexOf("?") > -1 ){
    var strQueryString = strHref.substr(strHref.indexOf("?")).toLowerCase();
    var aQueryString = strQueryString.split("&");
    for (var iParam = 0; iParam < aQueryString.length; iParam++ ) {
      if (aQueryString[iParam].indexOf(strParamName.toLowerCase() + "=") > -1 ) {
        var aParam = aQueryString[iParam].split("=");
        strReturn = aParam[1];
        break;
      }
    }
  }
  return unescape(strReturn);
}
