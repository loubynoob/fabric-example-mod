# Eclipse Client — milestone 2 (Fabric, Minecraft 26.1.2)

Included: HUD framework + editor (drag, scale, hex colors, per-HUD options), FPS / CPS / Keystrokes / Armor / Coords / Survival HUDs, FPS Guard (lowers render distance if avg FPS < 40, restores it later), toggle sprint, glass-style editor UI. Open the editor with Right Shift or the "Eclipse HUD" button on the title/pause screen.

Added in M2: Eclipse title screen (logo, Singleplayer/Multiplayer/Realms, Options + Eclipse UI/HUD + Quit at the bottom), Fuji panorama (bundled), corner branding on every screen, loading screen (logo + bar), brighter inventory backdrop, logo next to your own name tag and tab-list name (only you see it), 18 sky presets (tint sky/fog colour, not textured skyboxes), view bobbing on/off/vanilla (the vanilla crosshair never bobs). Everything is in the HUD editor's left column (Sky..., View bobbing, Bright inv, Name logo).

Added in M2b: glass-style buttons on every vanilla screen (replaces the button textures), hold-to-zoom (key C, FOV set in the editor: 30/40/50/60).

Still not done: textured skyboxes (stars/clouds), glass panels for inventory/containers, NeoForge/Forge. Skyboxify is not bundled (license unverified).

## Build (no PC needed)
1. On GitHub, open FabricMC/fabric-example-mod -> "Use this template" -> create your repo.
2. Delete its src/ folder, upload this zip's src/ folder.
3. Keep the template's build.gradle / gradle.properties (they hold the correct versions). Set minecraft_version = 26.1.2 there.
4. Actions tab -> build -> download the jar from build/libs.

## Mixins are written with require=0 so a wrong name is skipped instead of crashing; the feature just won't show. If something is missing in game, send me logs/latest.log.

## Lines most likely to need a fix (written without a compiler)
- HudModule.render: g.pose().pushMatrix()/translate/scale/popMatrix
- Mouse events in HudEditorScreen (MouseButtonEvent signatures)
- g.item / g.itemDecorations, mc.getFps()
- KeyMapping constructor + Category in EclipseClient
- DataComponents.TOOL / WEAPON in ArmorHud
- Toggle sprint logic in EclipseClient is a simple approximation
Send me the build log errors and I'll fix them.
- Mixin target names: Screen.extractBackground/extractTransparentBackground, LogoRenderer, LoadingOverlay.extractRenderState, PlayerTabOverlay.getNameForDisplay, Player.getDisplayName, ClientLevel.getSkyColor
- Title screen: RealmsMainScreen import path, OptionsScreen constructor
a
