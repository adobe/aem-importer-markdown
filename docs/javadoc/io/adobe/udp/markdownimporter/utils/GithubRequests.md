#Class GithubRequests
Package [io.adobe.udp.markdownimporter.utils](README.md)<br>

> *java.lang.Object* > [GithubRequests](GithubRequests.md)






##Summary
####Constructors
| Visibility | Signature |
| --- | --- |
| **public** | [GithubRequests](#githubrequests)() |

####Methods
| Type and modifiers | Method signature |
| --- | --- |
| **public static** *org.apache.sling.commons.json.JSONObject* | [executeDiffRequest](#executediffrequeststring-string)(*java.lang.String* url, *java.lang.String* token) |
| **public static** *org.apache.sling.commons.json.JSONObject* | [executeTreeRequest](#executetreerequeststring-string-boolean)(*java.lang.String* url, *java.lang.String* token, **boolean** recursive) |
| **public static** *java.lang.String* | [getBranchSha](#getbranchshastring-string)(*java.lang.String* url, *java.lang.String* token) |
| **public static** *java.util.Date* | [getCommitTimestamp](#getcommittimestampstring-string)(*java.lang.String* url, *java.lang.String* token) |
| **public static** *java.lang.String* | [getDefaultBranch](#getdefaultbranchstring-string)(*java.lang.String* url, *java.lang.String* token) |
| **public static** *java.lang.String* | [getFileUrl](#getfileurlstring-string)(*java.lang.String* url, *java.lang.String* token) |
| **public static** *java.util.Map*<*java.lang.String*, *java.lang.String*> | [getShaMapping](#getshamappingstring-list-string)(*java.lang.String* url, *java.util.List*<*java.lang.String*> branches, *java.lang.String* token) |

---


##Constructors
####GithubRequests()
> 


---


##Methods
####executeDiffRequest(String, String)
> 


---

####executeTreeRequest(String, String, boolean)
> 


---

####getBranchSha(String, String)
> 


---

####getCommitTimestamp(String, String)
> 


---

####getDefaultBranch(String, String)
> 


---

####getFileUrl(String, String)
> 


---

####getShaMapping(String, List<String>, String)
> 


---

---

[![Marklet](https://img.shields.io/badge/Generated%20by-Marklet-green.svg)](https://github.com/Faylixe/marklet)