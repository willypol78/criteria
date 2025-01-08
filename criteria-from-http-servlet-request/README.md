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

### ✅ Testing

To facilitate the testing of the criteria, you can use the provided in test packages [object mothers](https://www.martinfowler.com/bliki/ObjectMother.html)

## ➕ Other implementations

- We have [another implementation in TypeScript](https://github.com/CodelyTV/typescript-criteria) with converters for Next.js and URL. 🙌
- We have [another implementation in PHP](https://github.com/CodelyTV/php-criteria) with converters for Laravel and Symfony. 🙌

## 🚀 Release

...
