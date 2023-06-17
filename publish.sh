# TODO: Move to a gradle task. I don't know how to make gradle tasks run in this specific order though.
./gradlew ':quilt:curseforge'
./gradlew ':forge:curseforge'
./gradlew ':fabric:curseforge'

./gradlew ':quilt:modrinth'
./gradlew ':forge:modrinth'
./gradlew ':fabric:modrinth'
