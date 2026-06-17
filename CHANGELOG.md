# Tx3 Toolkit — Changelog

## [2.0.0] — 2026-06-15

### Changed (breaking)

- **2026.1+ IDE compatibility** — Compatibility range moved to builds 243 → 261
  (2024.3 through 2026.1). The previous `untilBuild` of `253.*` gated the plugin
  out of all 2026.x IDEs entirely.
- **Minimum supported IDE raised to 2024.3 (build 243)** — Building against the
  2026.1 SDK emits an `invokespecial` to `InlayHintsProvider.getSettingsLanguage()`,
  a default interface method absent before 243, so 233/241/242 would fail at
  runtime. Users on older IDEs should remain on 1.0.4.
- **Build migrated to IntelliJ Platform Gradle Plugin 2.x** — Replaced the
  deprecated `org.jetbrains.intellij` (1.x) plugin with
  `org.jetbrains.intellij.platform` (2.16.0). The plugin now builds against the
  2026.1 SDK.
- **Toolchain upgraded to JDK 21, Gradle 9.0, and Kotlin 2.3.20** — Required by
  the modern IntelliJ Platform; the 2026.1 SDK ships Kotlin 2.3.0 metadata that
  the previous 1.9.x compiler could not read. CI now provisions JDK 21.

### Added

- **Plugin Verifier in CI** — Binary API compatibility is now verified across
  builds 243 → 261 (2024.3, 2025.3, 2026.1) on every change via `verifyPlugin`.

## [1.0.4] — 2026-02-22

### Added

- **Type aliases** — Parser now supports `type AssetName = Bytes;` syntax for
  declaring type aliases.
- **Union types** — Parser now supports `type Credential = VerKey | Script;`
  syntax for declaring union types in type alias context.
- **Anonymous record types** — Parser now supports
  `type Addr = { hash: Bytes, staking: Bytes };` for inline record type
  definitions in type alias context.
- **Variant tuple wrapping** — Parser now supports `Some(Int)` syntax for
  variant cases with positional type parameters.
- **Array type suffix** — Parser now supports `Int[]` postfix syntax for array
  types in any type position (parameters, fields, aliases).
- **List indexing** — Parser now supports `items[0]` index expressions as a
  postfix operator alongside field access.
- **Ternary expressions** — Parser now supports `flag ? a : b` conditional
  expressions with right-associative nesting.
- **Spread operator trailing comma warning** — A warning is now shown when a
  trailing comma follows a spread expression (`...source,`) since the upstream
  `trix` compiler does not accept it.

### Fixed

- **Lexer crash on incremental re-lexing** — `InvalidStateException:
  Unexpected termination offset for lexer FlexAdapter` when pressing Enter or
  editing documents containing characters outside the 8-bit range. Fixed by
  switching the JFlex lexer from `%8bit` to `%unicode`.
- **Field access on soft keywords** — Property access expressions like
  `source.amount` failed when the field name was a soft keyword (`amount`,
  `from`, `to`, etc.). Fixed `parsePostfixExpr` to accept soft keywords after
  `.` using `expectIdentifier()`.
- **False trailing comma errors on tx params** — The trailing comma enforcement
  was incorrectly applied to transaction parameter declarations (which use
  comma-separated syntax, not comma-terminated). Removed the check from
  `Tx3TxParamImpl`.

### Upstream Compatibility Notes

The Tx3 language specification documents several expression operators that are
not yet implemented in the upstream `trix` compiler. The plugin parser supports
all spec-documented constructs for forward compatibility, but the following
operators will produce errors when checked with `trix check`:

- **Arithmetic:** `*`, `/` (only `+` and `-` are implemented upstream)
- **Comparison:** `==`, `!=`, `<`, `>`, `<=`, `>=`
- **Logical:** `&&`, `||`, `!`
- **Ternary:** `? :`

The test fixture (`main.tx3`) has been validated against `trix check` and only
uses constructs supported by the upstream compiler.

### Infrastructure

- **GitHub Actions CI pipeline** — Added `ci.yml` workflow that runs on push
  and PR to `main`: generates the lexer, builds the plugin, runs the full test
  suite, and uploads test reports on failure.
- **Trix toolchain validation** — CI pipeline includes a `trix-check` job that
  installs the Tx3 toolchain and runs `trix check` against the test data
  project to validate fixtures against the upstream compiler.
- **Release workflow** — Added `release.yml` for automated plugin releases.
- **Upstream compatibility check** — Added `upstream-check.yml` workflow for
  monitoring upstream Tx3 language changes.
- **Gradle wrapper committed** — Added `gradlew` and `gradlew.bat` for
  reproducible builds across environments and CI.
- **Parser test suite** — Added PSI tree snapshot tests covering all 7 new
  language constructs (type aliases, union types, anonymous records, variant
  tuples, array types, index expressions, ternary expressions) plus a combined
  features test and nested ternary test.
- **Lexer test suite** — Added tests for `?` and `|` token recognition,
  verified `||` (logical OR) is not broken by the new `|` (pipe) token.

## [1.0.3] — 2026-02-22

### Fixed

- **Plugin crash on disable/enable** — `ClassCastException` when toggling the
  plugin in a live session. The previous fix (1.0.2) used a `try/catch` that
  could never intercept the error because the JVM enforces parameter type casts
  at the bytecode level before any method body executes. Fixed properly by
  switching to `NoSettings`, eliminating classloader serialization entirely.
- **Plugin icon invisible on light themes** — Added a dark rounded background to
  `pluginIcon.svg` for visibility on light IDE themes and the JetBrains
  Marketplace. Added `pluginIcon_dark.svg` (transparent background) for dark IDE
  themes, following the JetBrains dual-icon convention.
- **Deprecated API removed** — Replaced `getDefaultCommonSettings()` with the
  `customizeDefaults(commonSettings, indentOptions)` override, eliminating the
  deprecated API warning from the JetBrains Marketplace auto-verifier. Removed
  the unused `Tx3CodeStyleSettings` class along with it.

### Added

- **Code folding for `type` declarations** — `type Vote { … }` folds with a
  field or variant count in the placeholder (e.g. `type Vote { 3 fields }`).
- **Code folding for `env` blocks** — Top-level `env { … }` blocks now fold with
  a field count placeholder.
- **Code folding for `locals` blocks** — `locals { … }` blocks inside `tx`
  declarations now fold.
- **Code folding for inline record literals** — Record literals used as field
  values (e.g., a `datum`) fold when they contain two or more fields, with the
  type name as the placeholder (e.g. `State { … }`).
- **Code folding for variant construction expressions** —
  `TypeName::CaseName { … }` expressions used as `datum` or `redeemer` values
  fold when they contain two or more fields, with the full qualified name as the
  placeholder (e.g., `ShipCommand::MoveShip { … }`).
- **Dynamic plugin loading** — Added `require-restart="false"` to `plugin.xml`
  so installing and updating the plugin no longer requires an IDE restart. Note:
  uninstall still requires a restart due to IntelliJ's file type manager being
  non-dynamic.

## [1.0.2] — 2026-02-22

### Fixed

- **Startup crash** — `ClassCastException` when IntelliJ passes inlay hint
  `Settings` serialized by a previous classloader instance (e.g., after a plugin
  update or IDE restart without full reinstalling). The provider now falls back
  to default settings instead of crashing.

## [1.0.1] - 2026-02-22

- **Plugin ID rejected** — The plugin ID `io.txpipe.tx3.intellij` was rejected
  by the JetBrains Marketplace validator as it must not contain the word
  `intellij`. Renamed to `io.txpipe.tx3`.
- **`env` block parse error** — Fields inside `env { ... }` blocks were
  incorrectly flagged as errors due to a double-brace consumption bug introduced
  during the `parseRecordFieldBlock` refactor. The opening `{` was being
  consumed twice, causing the first field name to be parsed as an unexpected
  token.
- **Builtin type highlighting lost** — `Int`, `Bytes`, `Bool` and other builtin
  types were losing their syntax highlighting inside `type` and `record`
  declarations. The annotator's semantic pass was overriding lexer-level token
  colors without re-applying them on `TypeRef` nodes.
- **Update plugin icon** — The plugin icon was updated to match the branding
  guidelines from JetBrains to ensure it appears consistently on the marketplace
  and in the plugin manager.

## [1.0.0] — 2026-02-22

### Added

- Syntax highlighting with semantic color tokens for all Tx3 constructs
- Smart code completion for keywords, block fields, types, and user-defined
  symbols
- Type-aware inlay hints showing parameter and record field types inline
- Code folding for `tx`, `type`, `record`, `party`, and `policy` blocks
- Structure view and file outliner for quick navigation
- Live templates for common Tx3 patterns (`tx`, `party`, `record`, `input`,
  `output`)
- Error annotations with quick-fixes for missing trailing commas
- Built-in Tx3 color scheme (dark theme)
- Auto-closing braces, brackets, and parentheses
- Formatter with correct 4-space indentation and block-aware enter handling
- New File action with template selection (Blank, Simple Transfer, Vesting
  Contract)
- Support for all Tx3 block types: `input`, `input*`, `output`, `burn`, `mint`,
  `locals`, `collateral`, `reference`, `signers`, `validity`, `metadata`
- Support for `env`, `asset`, `type`, `record`, `party`, `policy` declarations
- Support for variant types, generic types, and field access expressions