# Car Inventory Management Console Application

This Java console application manages a car inventory by parsing CSV and XML files, filtering and sorting car data, and displaying the results in different formats (Table, JSON, XML). It supports flexible filtering, sorting by multiple criteria, and multiple output formats with an easy-to-use console menu.

---

## Features

- **Parse car data** from:
    - CSV file (brands and release dates)
    - XML file (models, types, prices in multiple currencies)
- **Filter inventory** by brand, price (any currency), and release date.
- **Sort inventory** by price, release date, or by car type & preferred currency.
- **Output formats**: Tabular (console table), JSON, and XML.
- **Robust user input handling** with input validation and re-prompting.
- **Currency normalization and dynamic default price updating** for sorting and display.
- Console menu-driven interface with color-coded messages for clarity.

---

## Project Structure
````
src/
├── Main.java # Entry point with main loop and user interaction
├── interfaces/
│ └── CarParser.java # Interface for car parsing
├── models/
│ ├── Car.java # Car data model
│ └── OutputFormat.java # Enum for output format types
├── parser/
│ └── CarParserImplementation.java # Concrete parser implementation combining CSV & XML data
├── util/
│ ├── CsvReader.java # CSV file parsing utility
│ ├── DateFormatter.java # Date formatting helper
│ ├── FilterUtil.java # Filtering logic and interactive console input handling
│ ├── PrintUtil.java # Printing utilities for console output and formatting
│ ├── SortUtil.java # Sorting logic and interactive console input handling
│ ├── XmlParser.java # XML parsing utility
│ └── ViewFormatter.java # Formatting cars for display in table, JSON, XML

````
---

## How It Works

1. **Data Parsing**

    - Parses car information from CSV (brand, release date) and XML (model, type, prices).
    - Merges data into unified `Car` objects.

2. **User Interaction**

    - Console menu with options:
        - Change output format (Table, JSON, XML).
        - Filter inventory by brand, price, release date.
        - Sort inventory by price, date, or by type with preferred currency.
        - View inventory.
        - Exit program.
    - Loop runs until exit command.

3. **Filtering**

    - Case-insensitive brand filtering.
    - Price filter checks all currency prices.
    - Release date filter with input validation.

4. **Sorting**

    - Ascending/descending by price or date.
    - Sort by car type & currency preference (updates default price accordingly).

5. **Output**

    - Tabular format with dynamic currency columns.
    - JSON and XML with nested price mappings.
    - Color-coded console messages for errors and status.

---

## Setup Instructions

1. **Prerequisites**

    - Java JDK 17+ installed.
    - IDE or command line for Java.

2. **Project Files**

    - Place `CarsBrand.csv` and `carsType.xml` in the project root or resources folder.

3. **Compile and Run**

    - Command line example:
      ```bash
      javac -d out src/**/*.java
      java -cp out src.Main
      ```
    - Or run `Main` class from your IDE.

4. **Usage**

    - Follow on-screen menu prompts.
    - Enter valid inputs as requested.
    - Use digit inputs to exit input loops where applicable.

---

## Example Usage

- Default view shows tabular inventory.
- Change format with option `1`.
- Filter with option `2`.
- Sort with option `3`.
- View full inventory with option `4`.
- Exit with option `5`.

---

## Code Highlights

- Modular design with separation of concerns.
- Clean, readable, and maintainable code using modern Java features.
- Robust user input validation and re-prompting.
- Extensible for future features and formats.

---

## Contributing

Contributions, issues, and feature requests are welcome!

---
