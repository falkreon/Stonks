package blue.endless.stonks.model;

/**
 * Represents a kind of loyalty reward paid to shareholders of some investment asset by the issuer or fund manager. This
 * reward can be paid in cash or stocks, and for the purposes of this assignment is awarded once each quarter as a
 * percentage of the shares held.
 */
public record Dividend(double percentage, DividendType type) {
}
