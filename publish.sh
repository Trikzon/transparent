# TODO: Move to a gradle task. I don't know how to make gradle tasks run in this specific order though.
./gradlew ':Quilt:curseforge'
./gradlew ':Forge:curseforge'
./gradlew ':Fabric:curseforge'

./gradlew ':Quilt:modrinth'
./gradlew ':Forge:modrinth'
./gradlew ':Fabric:modrinth'
