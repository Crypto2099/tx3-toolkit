import org.jetbrains.grammarkit.tasks.GenerateLexerTask
import org.jetbrains.grammarkit.tasks.GenerateParserTask
import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType
import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("java")
    // Kotlin must match the platform's bundled compiler: 2026.1 (build 261) ships
    // K2.3.20 metadata, which a 1.9.x compiler cannot read.
    id("org.jetbrains.kotlin.jvm") version "2.3.20"
    // Migrated from the deprecated org.jetbrains.intellij (1.x) to the
    // IntelliJ Platform Gradle Plugin (2.x) for 2026+ IDE compatibility.
    id("org.jetbrains.intellij.platform") version "2.16.0"
    id("org.jetbrains.grammarkit") version "2022.3.2.2"
}

group = "io.txpipe"
version = "2.0.0"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    // ── IntelliJ Platform target SDK + bundled deps ───────────────────────────
    intellijPlatform {
        // Build against 2026.1 (build 261). Since 2025.3 (253), JetBrains no longer
        // publishes a separate IntelliJ IDEA Community ("IC") artifact — the unified
        // IntelliJ IDEA distribution (IntellijIdea) is used instead.
        // com.intellij.java is intentionally NOT bundled — the Tx3 plugin depends
        // only on platform + lang modules.
        create(IntelliJPlatformType.IntellijIdea, "2026.1")
        testFramework(TestFrameworkType.Platform)
    }

    testImplementation("junit:junit:4.13.2")
}

grammarKit {
    // Use GrammarKit's bundled JFlex and skeleton — pinning jflexRelease bypasses
    // the IntelliJ-specific skeleton and causes reset() / ZZ_CMAP generation errors.
    grammarKitRelease.set("2022.3")
}

intellijPlatform {
    // buildSearchableOptions spins up a full headless IDE to index plugin settings
    // for the Settings search bar. Not needed for a language plugin and flaky in CI.
    buildSearchableOptions.set(false)

    // The Tx3 plugin uses no GUI forms or @NotNull bytecode instrumentation, so the
    // Java instrumentation pass is unnecessary (and avoids pulling extra tooling).
    instrumentCode.set(false)

    pluginConfiguration {
        version.set(project.version.toString())
        ideaVersion {
            // sinceBuild is 243 (2024.3): building against the 2026.1 SDK bakes an
            // invokespecial to InlayHintsProvider.getSettingsLanguage(), a default
            // interface method absent before 243 — running on 233/241/242 would
            // throw NoSuchMethodError. The Plugin Verifier confirms 243 → 261.
            sinceBuild.set("243")
            untilBuild.set("261.*")
        }
        changeNotes.set("""
            <h3>2.0.0</h3>
            <ul>
              <li>Added compatibility with 2026.1+ JetBrains IDEs (build 261)</li>
              <li>Migrated the build to the IntelliJ Platform Gradle Plugin 2.x (the 1.x plugin is deprecated)</li>
              <li>Upgraded the toolchain to JDK 21, Gradle 9.0, and Kotlin 2.3.20, matching the modern IntelliJ Platform</li>
              <li>Raised the minimum supported IDE to 2024.3 (build 243); earlier IDEs should stay on 1.0.4</li>
              <li>Added Plugin Verifier coverage across builds 243 through 261 in CI</li>
            </ul>
            <h3>1.0.4</h3>
            <ul>
              <li>Added parser support for type aliases (<code>type AssetName = Bytes;</code>)</li>
              <li>Added parser support for union types (<code>type Credential = VerKey | Script;</code>)</li>
              <li>Added parser support for anonymous record types (<code>type Addr = { hash: Bytes, staking: Bytes };</code>)</li>
              <li>Added parser support for variant tuple wrapping (<code>Some(Int)</code>)</li>
              <li>Added parser support for array type suffix (<code>Int[]</code>)</li>
              <li>Added parser support for list indexing (<code>items[0]</code>)</li>
              <li>Added parser support for ternary expressions (<code>flag ? a : b</code>)</li>
              <li>Added warning annotation for trailing comma after spread operator (<code>...source,</code>)</li>
              <li>Fixed lexer crash (InvalidStateException) during incremental re-lexing by switching from %8bit to %unicode</li>
              <li>Fixed field access on soft keywords (e.g. <code>source.amount</code>) failing after <code>.</code></li>
              <li>Fixed false trailing comma errors on transaction parameter declarations</li>
              <li>Added GitHub Actions CI pipeline with build, test, and trix-check jobs</li>
              <li>Added release and upstream compatibility check workflows</li>
              <li>Added Gradle wrapper for reproducible builds</li>
              <li>Added parser and lexer tests covering all new language constructs</li>
            </ul>
            <h3>1.0.3</h3>
            <ul>
              <li>Fixed ClassCastException crash when disabling/enabling the plugin in a live session by switching to NoSettings, eliminating classloader serialization entirely</li>
              <li>Fixed plugin icon invisible on light IDE themes; added pluginIcon_dark.svg for dark themes</li>
              <li>Replaced deprecated getDefaultCommonSettings() with customizeDefaults(); removed unused Tx3CodeStyleSettings class</li>
              <li>Code folding for type declarations, env blocks, and locals blocks</li>
              <li>Code folding for inline record literals with 2 or more fields</li>
              <li>Code folding for variant construction expressions (TypeName::CaseName { … }) with 2 or more fields</li>
              <li>Dynamic plugin loading — installing and updating no longer requires an IDE restart</li>
            </ul>
            <h3>1.0.2</h3>
            <ul>
              <li>Attempted fix for ClassCastException caused by stale inlay hint Settings across classloader instances (superseded by 1.0.3)</li>
            </ul>
            <h3>1.0.1</h3>
            <ul>
              <li>Fixed plugin ID rejected by JetBrains Marketplace validator (renamed to io.txpipe.tx3)</li>
              <li>Fixed env block fields incorrectly flagged as errors due to double-brace consumption in parser</li>
              <li>Fixed builtin types (Int, Bytes, Bool, etc.) losing syntax highlighting inside type and record declarations</li>
              <li>Updated plugin icon to meet JetBrains branding guidelines</li>
            </ul>
            <h3>1.0.0</h3>
            <ul>
              <li>Initial release with full Tx3 language support</li>
            </ul>
        """.trimIndent())
    }

    // Plugin Verifier — validates the binary API surface against the full
    // supported range so 2026 API breaks are caught without installing each IDE.
    pluginVerification {
        ides {
            // Verify the full supported range (sinceBuild 243 → untilBuild 261).
            // Community ("IC") was published through 2024.3; from 2025.3 (253)
            // onward only the unified IntelliJ IDEA distribution exists.
            create(IntelliJPlatformType.IntellijIdeaCommunity, "2024.3")
            create(IntelliJPlatformType.IntellijIdea, "2025.3")
            create(IntelliJPlatformType.IntellijIdea, "2026.1")
        }
    }

    signing {
        certificateChain.set(providers.environmentVariable("CERTIFICATE_CHAIN"))
        privateKey.set(providers.environmentVariable("PRIVATE_KEY"))
        password.set(providers.environmentVariable("PRIVATE_KEY_PASSWORD"))
    }

    publishing {
        token.set(providers.environmentVariable("PUBLISH_TOKEN"))
    }
}

// ── Lexer generation ──────────────────────────────────────────────────────────
val generateLexer = tasks.named<GenerateLexerTask>("generateLexer") {
    sourceFile.set(file("src/main/kotlin/io/txpipe/tx3/intellij/lexer/Tx3Lexer.flex"))
    // targetOutputDir replaces the deprecated targetDir/targetClass pair; the
    // generated class name comes from the %class directive in the .flex file.
    targetOutputDir.set(file("src/main/gen/io/txpipe/tx3/intellij/lexer"))
    purgeOldFiles.set(true)
}

// ── Parser generation (disabled — parser is hand-written) ─────────────────────
tasks.named<GenerateParserTask>("generateParser") {
    enabled = false
}

sourceSets {
    main {
        java.srcDirs("src/main/gen")
        kotlin.srcDirs("src/main/kotlin")
        resources.srcDirs("src/main/resources")
    }
    test {
        kotlin.srcDirs("src/test/kotlin")
        resources.srcDirs("src/test/testData")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    jvmToolchain(21)
}

tasks {
    // Fix: IntelliJ platform and our resources both contribute colorscheme XMLs;
    // keep ours and silently drop duplicates from the platform side.
    processResources {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    compileKotlin {
        dependsOn(generateLexer)
    }
    compileJava {
        dependsOn(generateLexer)
    }

    test {
        systemProperty("idea.test.src.dir", "${project.projectDir}/src/test/testData")
    }
}
