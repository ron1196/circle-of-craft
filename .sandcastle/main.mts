import { run, claudeCode } from "@ai-hero/sandcastle";

// Simple loop: an agent that picks open GitHub issues one by one and closes them.
// Run this with: npx tsx .sandcastle/main.mts
// Or add to package.json scripts: "sandcastle": "npx tsx .sandcastle/main.mts"

await run({
  // A name for this run, shown as a prefix in log output.
  name: "worker",

  // The agent provider. Pass a model string to claudeCode() — sonnet balances
  // capability and speed for most tasks. Switch to claude-opus-4-6 for harder
  // problems, or claude-haiku-4-5-20251001 for speed.
  agent: claudeCode("claude-opus-4-6"),

  // Path to the prompt file. Shell expressions inside are evaluated inside the
  // sandbox at the start of each iteration, so the agent always sees fresh data.
  promptFile: "./.sandcastle/prompt.md",

  // Maximum number of iterations (agent invocations) to run in a session.
  // Each iteration works on a single issue. Increase this to process more issues
  // per run, or set it to 1 for a single-shot mode.
  maxIterations: 3,

  // Lifecycle hooks — commands that run inside the sandbox at specific points.
  hooks: {
    // onSandboxReady runs once after the sandbox is initialised and the repo is
    // synced in, before the agent starts. Use it to install dependencies or run
    // any other setup steps your project needs.
    onSandboxReady: [
      { command: "java -version && chmod +x gradlew" },
      { command: "./gradlew --no-daemon tasks 2>&1 | tail -20 || echo 'WARN: Gradle setup failed — compilation may not work'" },
    ],
  },
});
