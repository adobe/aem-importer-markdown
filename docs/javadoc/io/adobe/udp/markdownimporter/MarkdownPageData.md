#Class MarkdownPageData
Package [io.adobe.udp.markdownimporter](README.md)<br>

> *java.lang.Object* > [MarkdownPageData](MarkdownPageData.md)

All implemented interfaces :
> [PageData](PageData.md)




##Summary
####Constructors
| Visibility | Signature |
| --- | --- |
| **public** | [MarkdownPageData](#markdownpagedatastring-string-templatemapper-string)(*java.lang.String* resourceType, *java.lang.String* template, [TemplateMapper](TemplateMapper.md) templateMapper, *java.lang.String* designPath) |
| **public** | [MarkdownPageData](#markdownpagedatalist-string-list)(*java.util.List*<*java.lang.String*> yamlProperties, *java.lang.String* title, *java.util.List*<*java.util.HashMap*<*java.lang.String*, *java.lang.String*>> components) |

####Methods
| Type and modifiers | Method signature |
| --- | --- |
| **public** *java.util.List*<*java.util.HashMap*<*java.lang.String*, *java.lang.String*>> | [getComponents](#getcomponents)() |
| **public** *java.util.Map*<*java.lang.String*, *java.io.File*> | [getImages](#getimages)() |
| **public** [TemplateMapper](TemplateMapper.md) | [getTemplateMapper](#gettemplatemapper)() |
| **public** *java.lang.String* | [getTitle](#gettitle)() |
| **public** *java.util.List*<*java.lang.String*> | [getYamlProperties](#getyamlproperties)() |
| **public** **void** | [setBranch](#setbranchstring)(*java.lang.String* branch) |
| **public** **void** | [setGithubUrl](#setgithuburlstring)(*java.lang.String* githubUrl) |
| **public** **void** | [setNavOrder](#setnavorderlong)(**long** navOrder) |
| **public** **void** | [setTemplateFromYaml](#settemplatefromyamlstring)(*java.lang.String* name) |
| **public** **void** | [setTitle](#settitlestring)(*java.lang.String* title) |
| **public** *java.util.Map*<*java.lang.String*, *java.lang.Object*> | [toContent](#tocontent)() |

---


##Constructors
####MarkdownPageData(String, String, TemplateMapper, String)
> 


---

####MarkdownPageData(List<String>, String, List<HashMap<String, String>>)
> 


---


##Methods
####getComponents()
> 


---

####getImages()
> 


---

####getTemplateMapper()
> 


---

####getTitle()
> 


---

####getYamlProperties()
> 


---

####setBranch(String)
> 


---

####setGithubUrl(String)
> 


---

####setNavOrder(long)
> 


---

####setTemplateFromYaml(String)
> 


---

####setTitle(String)
> 


---

####toContent()
> 


---

---

[![Marklet](https://img.shields.io/badge/Generated%20by-Marklet-green.svg)](https://github.com/Faylixe/marklet)