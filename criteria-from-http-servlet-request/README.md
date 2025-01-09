<p align="center">
  <a href="https://codely.com">
    <picture>
      <source media="(prefers-color-scheme: dark)" srcset="https://codely.com/logo/codely_logo-dark.svg">
      <source media="(prefers-color-scheme: light)" srcset="https://codely.com/logo/codely_logo-light.svg">
      <img alt="Codely logo" src="https://codely.com/logo/codely_logo.svg">
    </picture>
  </a>
</p>

<h1 align="center">
  🎼 Criteria from Http Servlet Request
</h1>

<p align="center">
    <a href="https://github.com/CodelyTV"><img src="https://img.shields.io/badge/Codely-OS-green.svg?style=flat-square" alt="Codely Open Source projects"/></a>
    <a href="https://pro.codely.com"><img src="https://img.shields.io/badge/Codely-Pro-black.svg?style=flat-square" alt="Codely Pro courses"/></a>
</p>

## 📥 Installation

To install maven base criteria from http servlet request dependency add:

```gradle
dependencies {
    implementation("com.codely:criteria-from-http-servlet-request:1.0.0")
}
```

```mvn
<dependency>
    <groupId>com.codely</groupId>
    <artifactId>criteria-from-http-servlet-request</artifactId>
    <version>1.0.0</version>
</dependency>

```

Then, add criteria preferred transformer converter to:

- [Elasticsearch](./criteria-to-elasticsearch)
- [Jdbc](./criteria-to-jdbc)
- [Spring JPA Specification](./criteria-to-spring-jpa)

You can also create your custom transformer.

## 💻 Usage

The criteria converter expect an url with the following format:

* **filters**: An array of filters. Composed by:
    * **f**: The field to filter by.
    * **o**: The operator to apply. [You can see here] the valid operators list.
    * **v**: The value to filter by.
* **orderBy**: The field to order by.
* **orderType**: The order to apply. ASC or DESC.
* **size**: The number of items per page.
* **page**: The page number.

You can change this definition to match your needs in CriteriaFromHttpServletRequestUriConverter.java

```java
private static final String FILTERS = "f";
private static final String FIELD = "f";
private static final String OPERATOR = "o";
private static final String VALUE = "v";
```

### Url examples

Url with one filter and no order or pagination:

```
http://localhost:3000/api/users?f[0][f]=name&f[0][o]=CONTAINS&f[0][v]=Javi
```

Url with two filter, order and pagination:

```
http://localhost:3000/api/v1/users
     ?f[0][f]=name&f[0][o]=EQUAL&f[0][v]=John
     &f[1][o]=AND&f[2][o]=NOT&f[3][o]=(&f[4][f]=age&f[4][o]=GT&f[4][v]=18
     &f[5][o]=OR&f[6][f]=age&f[6][o]=%3E&f[6][v]=65&f[7][o]=)
     &orderBy=name
     &orderType=ASC
     &page=1
     &size=10
```

## ✅ Testing

To facilitate the testing of the criteria, you can use the provided in test packages [object mothers](https://www.martinfowler.com/bliki/ObjectMother.html)

## ➕ Other implementations

- We have [another implementation in TypeScript](https://github.com/CodelyTV/typescript-criteria) with converters for Next.js and URL. 🙌
- We have [another implementation in PHP](https://github.com/CodelyTV/php-criteria) with converters for Laravel and Symfony. 🙌

## 🚀 Release

...
