# README

## JSON Library Selection

Initially, I selected the Jackson library because it is known for efficiently handling large amounts of data, both in terms of speed and memory usage. However, upon further analysis, I realized that GSON would have been a more appropriate choice as it processes the conversion better incrementally event by event, which means better management when dealing with complex data structures.

When comparing Jackson and GSON with other alternatives, it is evident that both are well-documented, stable, and supported by active developer communities. However, the specific requirements of my task make GSON a more suitable option. Both libraries have their merits, but the incremental conversion of GSON being event by event is particularly advantageous for this assignment.

