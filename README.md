
# Spring源码解读

## 包扫描相关

入口 ClassPathBeanDefinitionScanner doScan()方法

- 1.扫描指定包下的class文件（加载符合条件的class文件），解析成BeanDefinition对象
- 2.初始化BeanDefinition的相关属性，设置 Lazy Primary DependsOn Role Description等属性
- 3.检验是否已经加载过该BeanDefinition对象，重复加载会报错
- 4.将校验通过的BeanDefinition对象注入到`beanDefinitionMap`中


## 初始化非懒加载的单例Bean

入口 DefaultListableBeanFactory preInstantiateSingletons()

- 1.循环beanName，获取合并后的bd对象（每个bean都对应bd对象，存在父子类关系的类会新生成一个bd对象---`RootBeanDefinition`>，放入合并后的bd集合Map中--->`mergedBeanDefinitions`）
- 2.是单例bean、非懒加载、非抽象bd，则开始创建对象
- 3.是factoryBean走特殊逻辑，非factoryBean直接创建bean，`getBean(beanName)`方法
- 4.所有非懒加载的单例bean全部加载完成后，会执行所有实现了`SmartInitializingSingleton`接口的bean的`afterSingletonsInstantiated`方法


## getBean 创建bean实例

入口 AbstractBeanFactory getBean()

- 1.推断构造方法，getBean的三个重载方法
- 2.通过name获取beanName
- 3.如果是单例bean，直接从单例池中获取，返回
- 4.else找出所有父类，获取`dependsOn`注解，先创建依赖的类
- 5.根据scope创建bean
  - 1.单例，直接从单例池中获取，没有则创建，放入单例池
  - 2.元型bean，每次创建新的bean
  - 3.其他类型的bean，根据scope，执行不同的逻辑，比如@Requestscope session request会将生成的bean放入其对应的作用域




# <img src="src/docs/spring-framework.png" width="80" height="80"> Spring Framework [![Build Status](https://ci.spring.io/api/v1/teams/spring-framework/pipelines/spring-framework-5.3.x/jobs/build/badge)](https://ci.spring.io/teams/spring-framework/pipelines/spring-framework-5.3.x?groups=Build") [![Revved up by Gradle Enterprise](https://img.shields.io/badge/Revved%20up%20by-Gradle%20Enterprise-06A0CE?logo=Gradle&labelColor=02303A)](https://ge.spring.io/scans?search.rootProjectNames=spring)

This is the home of the Spring Framework: the foundation for all [Spring projects](https://spring.io/projects). Collectively the Spring Framework and the family of Spring projects are often referred to simply as "Spring". 

Spring provides everything required beyond the Java programming language for creating enterprise applications for a wide range of scenarios and architectures. Please read the [Overview](https://docs.spring.io/spring/docs/current/spring-framework-reference/overview.html#spring-introduction) section as reference for a more complete introduction.

## Code of Conduct

This project is governed by the [Spring Code of Conduct](CODE_OF_CONDUCT.adoc). By participating, you are expected to uphold this code of conduct. Please report unacceptable behavior to spring-code-of-conduct@pivotal.io.

## Access to Binaries

For access to artifacts or a distribution zip, see the [Spring Framework Artifacts](https://github.com/spring-projects/spring-framework/wiki/Spring-Framework-Artifacts) wiki page.

## Documentation

The Spring Framework maintains reference documentation ([published](https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/) and [source](src/docs/asciidoc)), Github [wiki pages](https://github.com/spring-projects/spring-framework/wiki), and an
[API reference](https://docs.spring.io/spring-framework/docs/current/javadoc-api/). There are also [guides and tutorials](https://spring.io/guides) across Spring projects.

## Micro-Benchmarks

See the [Micro-Benchmarks](https://github.com/spring-projects/spring-framework/wiki/Micro-Benchmarks) Wiki page.

## Build from Source

See the [Build from Source](https://github.com/spring-projects/spring-framework/wiki/Build-from-Source) Wiki page and the [CONTRIBUTING.md](CONTRIBUTING.md) file.

## Continuous Integration Builds

Information regarding CI builds can be found in the [Spring Framework Concourse pipeline](ci/README.adoc) documentation.

## Stay in Touch

Follow [@SpringCentral](https://twitter.com/springcentral), [@SpringFramework](https://twitter.com/springframework), and its [team members](https://twitter.com/springframework/lists/team/members) on Twitter. In-depth articles can be found at [The Spring Blog](https://spring.io/blog/), and releases are announced via our [news feed](https://spring.io/blog/category/news).

## License

The Spring Framework is released under version 2.0 of the [Apache License](https://www.apache.org/licenses/LICENSE-2.0).
