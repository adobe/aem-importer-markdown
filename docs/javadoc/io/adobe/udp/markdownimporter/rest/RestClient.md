#Class RestClient
Package [io.adobe.udp.markdownimporter.rest](README.md)<br>

> *java.lang.Object* > [RestClient](RestClient.md)



Rest Client Utility Class


##Summary
####Constructors
| Visibility | Signature |
| --- | --- |
| **public** | [RestClient](#restclientstring)(*java.lang.String* url) |
| **public** | [RestClient](#restclientstring-map)(*java.lang.String* url, *java.util.Map*<*java.lang.String*, *java.lang.String*> reqParam) |

####Methods
| Type and modifiers | Method signature |
| --- | --- |
| **public** **void** | [addHeader](#addheaderstring-string)(*java.lang.String* name, *java.lang.String* value) |
| **public** [RestClientResponse](RestClientResponse.md) | [doGet](#doget)() |

---


##Constructors
####RestClient(String)
> Rest Client constructor


---

####RestClient(String, Map<String, String>)
> Rest Client constructor


---


##Methods
####addHeader(String, String)
> 


---

####doGet()
> Performs the GET

> **Returns**
* String

> **Throws**
* *java.io.IOException* 


---

---

[![Marklet](https://img.shields.io/badge/Generated%20by-Marklet-green.svg)](https://github.com/Faylixe/marklet)