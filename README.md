# decibel

Scala library providing noise functionality designed for turn-based simulation games

## Example

```scala
  val noise = NoiseGrid.from(FbmNoise(PerlinNoise(), octaves = 1), 1024, 1024)
  val noise1 = NoiseGrid.from(TurbulenceNoise(PerlinNoise()), 1024, 1024)
  val noise2 = NoiseGrid.from(FbmNoise(VoronoiNoise(density = 4, distanceFn = Vector2.sqrDistance), frequency = 1 / 128d, octaves = 1), 1024, 1024)
  noise.write("stuff.png")
```

## Todo

* Is using default parameter values a problem? See Wart.DefaultArguments in build.sbt.
* Test coverage is poor - improve
* Better examples
