## CDI Servlet Example

Build using:

```bash
]$ mvn clean verify
```

After building you can run the resulting projects __exploded__, using:

```sh
]$ mvn bnd-run:run@equinox
#OR
]$ mvn bnd-run:run@felix
```

OR the __executable jars__:

```bash
]$ java -jar target/equinoxhttp.jar
#OR
]$ java -jar target/felixhttp.jar
```

Whatever the case visit the address `http://localhost:8080` in your _fav_ browser.