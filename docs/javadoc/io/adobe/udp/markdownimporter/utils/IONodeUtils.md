#Class IONodeUtils
Package [io.adobe.udp.markdownimporter.utils](README.md)<br>

> *java.lang.Object* > [IONodeUtils](IONodeUtils.md)






##Summary
####Constructors
| Visibility | Signature |
| --- | --- |
| **public** | [IONodeUtils](#ionodeutils)() |

####Methods
| Type and modifiers | Method signature |
| --- | --- |
| **public static** **void** | [addPlaceHolderTemplate](#addplaceholdertemplatestring-string-string-set-map-inputconfig)(*java.lang.String* rootPath, *java.lang.String* filePath, *java.lang.String* githubFilePath, *java.util.Set*<*java.lang.String*> files, *java.util.Map*<*java.lang.String*, [PageData](../PageData.md)> pages, [InputConfig](../InputConfig.md) config) |
| **public static** *java.lang.String* | [escapeBackslash](#escapebackslashstring)(*java.lang.String* path) |
| **public static** *java.lang.String* | [extractName](#extractnamestring)(*java.lang.String* path) |
| **public static** *java.lang.String* | [getBranchPageName](#getbranchpagenamebranchrootinfo)([BranchRootInfo](../BranchRootInfo.md) rootInfo) |
| **public static** *java.lang.String* | [getBranchPageName](#getbranchpagenamestring)(*java.lang.String* branch) |
| **public static** *java.lang.String* | [getBranchPageName](#getbranchpagenamestring-string)(*java.lang.String* rootPath, *java.lang.String* branchName) |
| **public static** *java.lang.String* | [getBranchPagePath](#getbranchpagepathstring-branchrootinfo)(*java.lang.String* branch, [BranchRootInfo](../BranchRootInfo.md) rootInfo) |
| **public static** *java.lang.String* | [getBranchRootPath](#getbranchrootpathstring-string)(*java.lang.String* githubRoot, *java.lang.String* branch) |
| **public static** *java.lang.String* | [getFileFolder](#getfilefoldergithubdata-string)([GithubData](../GithubData.md) githubData, *java.lang.String* filePath) |
| **public static** **void** | [populatePath](#populatepathstring-session)(*java.lang.String* path, *javax.jcr.Session* session) |
| **public static** *java.lang.String* | [removeFirstSlash](#removefirstslashstring)(*java.lang.String* path) |
| **public static** *java.lang.String* | [removeMDExtensionFromPath](#removemdextensionfrompathstring)(*java.lang.String* path) |
| **public static** *java.lang.String* | [removeParams](#removeparamsstring)(*java.lang.String* path) |
| **public static** *java.lang.String* | [removeSlashAtEnd](#removeslashatendstring)(*java.lang.String* path) |
| **public static** *java.lang.String* | [replaceDotsInPath](#replacedotsinpathstring)(*java.lang.String* path) |
| **public static** *java.lang.String* | [stripFromExtension](#stripfromextensionstring)(*java.lang.String* path) |
| **public static** *java.lang.String* | [trimName](#trimnamestring)(*java.lang.String* name) |

---


##Constructors
####IONodeUtils()
> 


---


##Methods
####addPlaceHolderTemplate(String, String, String, Set<String>, Map<String, PageData>, InputConfig)
> 


---

####escapeBackslash(String)
> 


---

####extractName(String)
> 


---

####getBranchPageName(BranchRootInfo)
> 


---

####getBranchPageName(String)
> 


---

####getBranchPageName(String, String)
> 


---

####getBranchPagePath(String, BranchRootInfo)
> 


---

####getBranchRootPath(String, String)
> 


---

####getFileFolder(GithubData, String)
> 


---

####populatePath(String, Session)
> 


---

####removeFirstSlash(String)
> 


---

####removeMDExtensionFromPath(String)
> removes _md from rewritten path


---

####removeParams(String)
> 


---

####removeSlashAtEnd(String)
> 


---

####replaceDotsInPath(String)
> 


---

####stripFromExtension(String)
> 


---

####trimName(String)
> 


---

---

[![Marklet](https://img.shields.io/badge/Generated%20by-Marklet-green.svg)](https://github.com/Faylixe/marklet)