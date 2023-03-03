import java.util.ArrayList;
import java.util.List;

/**
 * Business Rules:
 * <p>
 * A warrior starts at level 1 and can progress all the way to 100.
 * A warrior starts at rank "Pushover" and can progress all the way to "Greatest".
 * The only acceptable range of rank values is "Pushover", "Novice", "Fighter", "Warrior", "Veteran",
 * "Sage", "Elite", "Conqueror", "Champion", "Master", "Greatest".
 * Warriors will compete in battles. Battles will always accept an enemy level to match against your own.
 * With each battle successfully finished, your warrior's experience is updated based on the enemy's level.
 * The experience earned from the battle is relative to what
 * the warrior's current level is compared to the level of the enemy.
 * A warrior's experience starts from 100. Each time the warrior's experience increases by another 100,
 * the warrior's level rises to the next level.
 * A warrior's experience is cumulative, and does not reset with each rise of level.
 * The only exception is when the warrior reaches level 100, with which the experience stops at 10000
 * At every 10 levels, your warrior will reach a new rank tier. (ex. levels 1-9 falls within "Pushover" tier,
 * levels 80-89 fall within "Champion" tier, etc.)
 * A warrior cannot progress beyond level 100 and rank "Greatest".
 */
public class Warrior {

    /**
     * Warrior level from 1 to 100
     */
    protected int level;
    /**
     * Warrior experience from 100 to 10000
     */
    protected int experience;
    /**
     * Warrior rank
     */
    protected String rank;
    /**
     * Warrior achievements
     */
    protected List<String> achievements;
    /**
     * Fights difficult
     */
    protected static String[] FIGHT_STATUS = new String[]{
            "Invalid level",
            "Easy fight",
            "A good fight",
            "An intense fight",
            "You've been defeated",
            "Not strong enough"
    };
    /**
     * Warriors ranks
     */
    protected static String[] WARRIOR_RANKS = new String[]{
            "Pushover",
            "Novice",
            "Fighter",
            "Warrior",
            "Veteran",
            "Sage",
            "Elite",
            "Conqueror",
            "Champion",
            "Master",
            "Greatest"
    };
    /**
     * Experience per level
     */
    protected static int EXPERIENCE_PER_LEVEL = 100;
    /**
     * Experience start
     */
    protected static int EXPERIENCE_START = 100;
    /**
     * Levels per rank
     */
    protected static int LEVELS_PER_RANK = 10;
    /**
     * Warriors level minimal
     */
    protected static int LEVEL_MIN = 1;
    /**
     * Warriors level maximum
     */
    protected static int LEVEL_MAX = 100;

    /**
     * Constructor
     */
    public Warrior() {
        experience = EXPERIENCE_START;
        achievements = new ArrayList<>();
        recheckStats();
    }

    /**
     * Get warriors level
     *
     * @return current level
     */
    protected int level() {
        return level;
    }

    /**
     * Get warriors experience
     *
     * @return current experience
     */
    protected int experience() {
        return experience;
    }

    /**
     * Get warriors rank
     *
     * @return current rank
     */
    protected String rank() {
        return rank;
    }

    /**
     * Get warriors achievements
     *
     * @return current achievements
     */
    protected List<String> achievements() {
        return achievements;
    }

    /**
     * Re-check warrior stats
     */
    protected void recheckStats() {
        if (experience > EXPERIENCE_PER_LEVEL * LEVEL_MAX) {
            experience = EXPERIENCE_PER_LEVEL * LEVEL_MAX;
        }
        level = experience / EXPERIENCE_PER_LEVEL;
        rank = WARRIOR_RANKS[level / LEVELS_PER_RANK];
    }

    /**
     * In addition to earning experience point from battles, warriors can also gain experience points from training.
     * Training will accept an array of three elements (except in java where you'll get 3 separated arguments):
     * the description, the experience points your warrior earns, and the minimum level requirement.
     * If the warrior's level meets the minimum level requirement, the warrior will receive the experience points
     * from it and store the description of the training. It should end up returning that description as well.
     * If the warrior's level does not meet the minimum level requirement, the warrior doesn't not receive
     * the experience points and description and instead returns "Not strong enough", without any archiving of the result.
     *
     * @param fight         training fight
     * @param experience    new experience
     * @param requiredLevel new lvl
     * @return training result
     */
    protected String training(String fight, int experience, int requiredLevel) {
        if (this.level < requiredLevel) {
            return FIGHT_STATUS[5];
        } else {
            this.achievements.add(fight);
            this.experience += experience;
            recheckStats();

            return fight;
        }
    }

    /**
     * Battle Progress Rules & Calculations:
     * <p>
     * If an enemy level does not fall in the range of 1 to 100, the battle cannot happen and should return "Invalid level".
     * Completing a battle against an enemy with the same level as your warrior will be worth 10 experience points.
     * Completing a battle against an enemy who is one level lower than your warrior will be worth 5 experience points.
     * Completing a battle against an enemy who is two levels lower or more than your warrior will give 0 experience points.
     * Completing a battle against an enemy who is one level higher or more than your warrior will accelerate your experience gaining.
     * The greater the difference between levels, the more experience your warrior will gain.
     * The formula is 20 * diff * diff where diff equals the difference in levels between the enemy and your warrior.
     * However, if your warrior is at least one rank lower than your enemy, and at least 5 levels lower,
     * your warrior cannot fight against an enemy that strong and must instead return "You've been defeated".
     * Every successful battle will also return one of three responses: "Easy fight", "A good fight", "An intense fight".
     * Return "Easy fight" if your warrior is 2 or more levels higher than your enemy's level.
     * Return "A good fight" if your warrior is either 1 level higher or equal to your enemy's level.
     * Return "An intense fight" if your warrior's level is lower than the enemy's level.
     *
     * @param level enemy level
     * @return fight difficult
     */
    protected String battle(int level) {
        //level range check
        if (level < LEVEL_MIN || level > LEVEL_MAX) {
            return FIGHT_STATUS[0];
        } else {
            if (this.level < level) {
                if (this.level / 10 != level / 10 && level - this.level >= 5) {
                    return FIGHT_STATUS[4];
                } else {
                    int diff = level - this.level;
                    this.experience += (20 * diff * diff);
                    recheckStats();
                }
                return FIGHT_STATUS[3];
            } else {
                //Completing a battle against an enemy with the same level as your warrior will be worth 10 experience points.
                if (this.level == level) {
                    this.experience += 10;
                    recheckStats();
                    return FIGHT_STATUS[2];
                }
                //Completing a battle against an enemy who is one level lower than your warrior will be worth 5 experience points.
                else if (this.level - level == 1) {
                    this.experience += 5;
                    recheckStats();
                    return FIGHT_STATUS[2];
                } else {
                    return FIGHT_STATUS[1];
                }
            }
        }
    }

}
