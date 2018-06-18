#Class GithubLinkServiceImpl
Package [io.adobe.udp.markdownimporter.services](README.md)<br>

> *java.lang.Object* > [GithubLinkServiceImpl](GithubLinkServiceImpl.md)

All implemented interfaces :
> [GithubLinkService](GithubLinkService.md)




##Summary
####Constructors
| Visibility | Signature |
| --- | --- |
| **public** | [GithubLinkServiceImpl](#githublinkserviceimpl)() |

####Methods
| Type and modifiers | Method signature |
| --- | --- |
| **public** *java.lang.String* | [getBranchesUrl](#getbranchesurlstring-inputconfig)(*java.lang.String* repositoryUrl, [InputConfig](../InputConfig.md) config) |
| **public** *java.lang.String* | [getCommitUrl](#getcommiturlgithubdata-string)([GithubData](../GithubData.md) githubData, *java.lang.String* sha) |
| **public** *java.lang.String* | [getContentUrl](#getcontenturlstring-string)(*java.lang.String* repositoryUrl, *java.lang.String* ref) |
| **public** *java.lang.String* | [getDiffUrl](#getdiffurlgithubdata-string)([GithubData](../GithubData.md) githubData, *java.lang.String* sha) |
| **public** *java.lang.String* | [getFileBlobUrl](#getfilebloburlgithubdata-string)([GithubData](../GithubData.md) githubData, *java.lang.String* githubFilePath) |
| **public** [GithubData](../GithubData.md) | [getGithubData](#getgithubdatastring-string-inputconfig)(*java.lang.String* repositoryUrl, *java.lang.String* branch, [InputConfig](../InputConfig.md) config) |
| **public** *java.lang.String* | [getGithubTreeUrl](#getgithubtreeurlstring-githubdata)(*java.lang.String* path, [GithubData](../GithubData.md) githubData) |
| **public** *java.lang.String* | [getPathFromUrl](#getpathfromurlstring-githubdata)(*java.lang.String* path, [GithubData](../GithubData.md) githubData) |
| **public** *java.lang.String* | [getPathInRepository](#getpathinrepositorystring-githubdata)(*java.lang.String* path, [GithubData](../GithubData.md) githubData) |
| **public** *java.lang.String* | [getReadmeUrl](#getreadmeurlgithubdata)([GithubData](../GithubData.md) githubData) |
| **public** *java.lang.String* | [getRepositoryApiUrl](#getrepositoryapiurlstring-inputconfig)(*java.lang.String* repositoryUrl, [InputConfig](../InputConfig.md) config) |
| **public** *java.lang.String* | [mapPathToUrl](#mappathtourlstring-githubdata)(*java.lang.String* path, [GithubData](../GithubData.md) githubData) |

---


##Constructors
####GithubLinkServiceImpl()
> 


---


##Methods
####getBranchesUrl(String, InputConfig)
> 


---

####getCommitUrl(GithubData, String)
> 


---

####getContentUrl(String, String)
> 


---

####getDiffUrl(GithubData, String)
> 


---

####getFileBlobUrl(GithubData, String)
> 


---

####getGithubData(String, String, InputConfig)
> 


---

####getGithubTreeUrl(String, GithubData)
> 


---

####getPathFromUrl(String, GithubData)
> 


---

####getPathInRepository(String, GithubData)
> 


---

####getReadmeUrl(GithubData)
> 


---

####getRepositoryApiUrl(String, InputConfig)
> 


---

####mapPathToUrl(String, GithubData)
> 


---

---

[![Marklet](https://img.shields.io/badge/Generated%20by-Marklet-green.svg)](https://github.com/Faylixe/marklet)