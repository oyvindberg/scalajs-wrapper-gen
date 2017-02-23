## Scala.js-react wrapper generator

This generator was written to handle the tedious task of updating the Material-UI
 wrappers in [scalajs-react-components](https://github.com/chandu0101/scalajs-react-components)

It currently supports Material-UI **0.17.0**.

The generator works by extracting all components and props from transpiled javascript code,
 and then match that with manually curated type definitions.
 
In a bit more detail: 

1. Resolve all javascript files needed given a set of component names and a base folder (`Require`)
2. Parse all the transpiled javascript code using Nashorn (`JsParser`)
3. Extract react components with all documented props, all comments for props, and all member functions (the various `Visitor`s)

These three steps are in the `requiresjs` module for now.

4. Parse and normalize input (`componentParsers`)
5. Resolve all types (the various `TypeMapper`s)
6. Convert to scala.js code (`Printer`)
7. Print to files (`Runner`, for now)

### Non-goals so far
- Other libraries. Most of the references to material-ui is abstracted away in the `mui` folder,
 but the whole `requiresjs` module is not generic enough to handle arbitrary code
 
## Usage
`sbt>run .../material-ui/build .../pr/scalajs-react-components/core/src/main/scala/chandu0101/scalajs/react/components/materialui`

Note that Material-UI **must** be successfully built first