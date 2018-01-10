#Class MarkdownMappings
Package [io.adobe.udp.markdownimporter.mappings](README.md)<br>

> *java.lang.Object* > [MarkdownMappings](MarkdownMappings.md)






##Summary
####Fields
| Type and modifiers | Field name |
| --- | --- |
| **public static final** | [markdownMapping](#markdownmapping) |

####Constructors
| Visibility | Signature |
| --- | --- |
| **public** | [MarkdownMappings](#markdownmappings)() |

####Methods
| Type and modifiers | Method signature |
| --- | --- |
| **public static** **void** | [configure](#configuremap)(*java.util.Map*<*java.lang.String*, *java.lang.String*> mappings) |
| **public static** [MarkdownNodeMapper](MarkdownNodeMapper.md) | [getMarkdownNodeMapper](#getmarkdownnodemappernode)(*com.vladsch.flexmark.ast.Node* node) |
| **public static** **boolean** | [hasMapping](#hasmappingnode)(*com.vladsch.flexmark.ast.Node* node) |

---


##Constructors
####MarkdownMappings()
> 


---


##Fields
####markdownMapping
> **public static final** *java.util.Map*<*java.lang.Class*, [MarkdownNodeMapper](MarkdownNodeMapper.md)>

> 

---


##Methods
####configure(Map<String, String>)
> 


---

####getMarkdownNodeMapper(Node)
> 


---

####hasMapping(Node)
> 


---

---

[![Marklet](https://img.shields.io/badge/Generated%20by-Marklet-green.svg)](https://github.com/Faylixe/marklet)