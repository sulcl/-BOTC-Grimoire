package com.example.data.model

object CharactersData {

    const val SCRIPT_TROUBLE_BREWING = "Trouble Brewing"
    const val SCRIPT_BAD_MOON_RISING = "Bad Moon Rising"
    const val SCRIPT_SECTS_AND_VIOLETS = "Sects & Violets"

    val ALL_SCRIPTS = listOf(
        SCRIPT_TROUBLE_BREWING,
        SCRIPT_BAD_MOON_RISING,
        SCRIPT_SECTS_AND_VIOLETS
    )

    val ALL_CHARACTERS: List<BotcCharacter> = listOf(
        // === TROUBLE BREWING: TOWNSFOLK ===
        BotcCharacter(
            id = "washerwoman",
            name = "Washerwoman",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_TROUBLE_BREWING,
            ability = "You start knowing that 1 of 2 players is a particular Townsfolk.",
            firstNightOrder = 33,
            firstNightReminder = "Show character token of a Townsfolk in play. Point to 2 players, one of whom is that Townsfolk.",
            defaultReminders = listOf("Townsfolk", "Wrong")
        ),
        BotcCharacter(
            id = "librarian",
            name = "Librarian",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_TROUBLE_BREWING,
            ability = "You start knowing that 1 of 2 players is a particular Outsider. (Or that zero are in play.)",
            firstNightOrder = 34,
            firstNightReminder = "Show character token of an Outsider in play. Point to 2 players (one is that Outsider). If no Outsiders, show '0'.",
            defaultReminders = listOf("Outsider", "Wrong")
        ),
        BotcCharacter(
            id = "investigator",
            name = "Investigator",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_TROUBLE_BREWING,
            ability = "You start knowing that 1 of 2 players is a particular Minion.",
            firstNightOrder = 35,
            firstNightReminder = "Show character token of a Minion in play. Point to 2 players, one of whom is that Minion.",
            defaultReminders = listOf("Minion", "Wrong")
        ),
        BotcCharacter(
            id = "chef",
            name = "Chef",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_TROUBLE_BREWING,
            ability = "You start knowing how many pairs of evil players there are sitting next to each other.",
            firstNightOrder = 36,
            firstNightReminder = "Show finger count (0, 1, 2, 3...) of adjacent evil player pairs."
        ),
        BotcCharacter(
            id = "empath",
            name = "Empath",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_TROUBLE_BREWING,
            ability = "Each night, you learn how many of your 2 alive neighbours are evil.",
            firstNightOrder = 37,
            otherNightsOrder = 53,
            firstNightReminder = "Show finger count (0, 1, or 2) representing evil alive neighbours.",
            otherNightsReminder = "Show finger count (0, 1, or 2) representing evil alive neighbours."
        ),
        BotcCharacter(
            id = "fortuneteller",
            name = "Fortune Teller",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_TROUBLE_BREWING,
            ability = "Each night, choose 2 players: you learn if either is a Demon. There is a good player that registers as a Demon to you.",
            firstNightOrder = 38,
            otherNightsOrder = 54,
            firstNightReminder = "Point to 2 players chosen. Nod yes if either is a Demon (or the Red Herring), shake no otherwise.",
            otherNightsReminder = "Point to 2 players chosen. Nod yes if either is a Demon (or the Red Herring), shake no otherwise.",
            defaultReminders = listOf("Red Herring")
        ),
        BotcCharacter(
            id = "undertaker",
            name = "Undertaker",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_TROUBLE_BREWING,
            ability = "Each night*, you learn which character died by execution today.",
            otherNightsOrder = 55,
            otherNightsReminder = "If a player was executed today, show that player's character token.",
            defaultReminders = listOf("Executed")
        ),
        BotcCharacter(
            id = "monk",
            name = "Monk",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_TROUBLE_BREWING,
            ability = "Each night*, choose a player (not yourself): they are safe from the Demon tonight.",
            otherNightsOrder = 13,
            otherNightsReminder = "Monk points to a player (not self). Place PROTECTED reminder token.",
            defaultReminders = listOf("Protected")
        ),
        BotcCharacter(
            id = "ravenkeeper",
            name = "Ravenkeeper",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_TROUBLE_BREWING,
            ability = "If you die at night, you are woken to choose a player: you learn their character.",
            otherNightsOrder = 52,
            otherNightsReminder = "If died tonight, wake Ravenkeeper. Ravenkeeper points to a player. Show that player's character token."
        ),
        BotcCharacter(
            id = "virgin",
            name = "Virgin",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_TROUBLE_BREWING,
            ability = "The 1st time you are nominated, if the nominator is a Townsfolk, they are executed immediately.",
            defaultReminders = listOf("No Ability")
        ),
        BotcCharacter(
            id = "slayer",
            name = "Slayer",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_TROUBLE_BREWING,
            ability = "Once per game, during the day, publicly choose a player: if they are the Demon, they die.",
            defaultReminders = listOf("No Ability")
        ),
        BotcCharacter(
            id = "soldier",
            name = "Soldier",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_TROUBLE_BREWING,
            ability = "You are safe from the Demon."
        ),
        BotcCharacter(
            id = "mayor",
            name = "Mayor",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_TROUBLE_BREWING,
            ability = "If only 3 players live & no execution occurs, your team wins. If you die at night, another player might die instead."
        ),

        // === TROUBLE BREWING: OUTSIDERS ===
        BotcCharacter(
            id = "butler",
            name = "Butler",
            type = CharacterType.OUTSIDER,
            edition = SCRIPT_TROUBLE_BREWING,
            ability = "Each night, choose a player (not yourself): tomorrow, you may only vote if they are voting too.",
            firstNightOrder = 39,
            otherNightsOrder = 67,
            firstNightReminder = "Butler chooses a player. Place MASTER reminder.",
            otherNightsReminder = "Butler chooses a player. Place MASTER reminder.",
            defaultReminders = listOf("Master")
        ),
        BotcCharacter(
            id = "drunk",
            name = "Drunk",
            type = CharacterType.OUTSIDER,
            edition = SCRIPT_TROUBLE_BREWING,
            ability = "You do not know you are the Drunk. You think you are a Townsfolk character, but you are not.",
            defaultReminders = listOf("Drunk"),
            setupRule = "The Drunk thinks they are a Townsfolk. Replace a Townsfolk token with Drunk in the Grimoire."
        ),
        BotcCharacter(
            id = "recluse",
            name = "Recluse",
            type = CharacterType.OUTSIDER,
            edition = SCRIPT_TROUBLE_BREWING,
            ability = "You might register as evil & as a Minion or Demon, even if dead."
        ),
        BotcCharacter(
            id = "saint",
            name = "Saint",
            type = CharacterType.OUTSIDER,
            edition = SCRIPT_TROUBLE_BREWING,
            ability = "If you die by execution, your team loses."
        ),

        // === TROUBLE BREWING: MINIONS ===
        BotcCharacter(
            id = "poisoner",
            name = "Poisoner",
            type = CharacterType.MINION,
            edition = SCRIPT_TROUBLE_BREWING,
            ability = "Each night, choose a player: they are poisoned tonight and tomorrow day.",
            firstNightOrder = 18,
            otherNightsOrder = 8,
            firstNightReminder = "Wake Poisoner. Poisoner points to a player. Place POISONED reminder.",
            otherNightsReminder = "Wake Poisoner. Poisoner points to a player. Remove previous POISONED, place new POISONED reminder.",
            defaultReminders = listOf("Poisoned")
        ),
        BotcCharacter(
            id = "spy",
            name = "Spy",
            type = CharacterType.MINION,
            edition = SCRIPT_TROUBLE_BREWING,
            ability = "Each night, you see the Grimoire. You might register as good & as a Townsfolk or Outsider, even if dead.",
            firstNightOrder = 49,
            otherNightsOrder = 68,
            firstNightReminder = "Wake Spy. Show the Grimoire for as long as they need.",
            otherNightsReminder = "Wake Spy. Show the Grimoire for as long as they need."
        ),
        BotcCharacter(
            id = "scarletwoman",
            name = "Scarlet Woman",
            type = CharacterType.MINION,
            edition = SCRIPT_TROUBLE_BREWING,
            ability = "If there are 5 or more players alive & the Demon dies, you become the Demon. (Travelers don't count)",
            otherNightsOrder = 20,
            otherNightsReminder = "If Demon died and 5+ alive players remain, Scarlet Woman becomes the Demon. Wake and show 'YOU ARE' Imp.",
            defaultReminders = listOf("Demon")
        ),
        BotcCharacter(
            id = "baron",
            name = "Baron",
            type = CharacterType.MINION,
            edition = SCRIPT_TROUBLE_BREWING,
            ability = "There are extra Outsiders in play. [+2 Outsiders]",
            setupRule = "Add 2 Outsiders and remove 2 Townsfolk during setup."
        ),

        // === TROUBLE BREWING: DEMON ===
        BotcCharacter(
            id = "imp",
            name = "Imp",
            type = CharacterType.DEMON,
            edition = SCRIPT_TROUBLE_BREWING,
            ability = "Each night*, choose a player: they die. If you kill yourself this way, a Minion becomes the Imp.",
            otherNightsOrder = 24,
            otherNightsReminder = "Imp points to a player. If chosen player is not protected, place DEAD reminder. If chose self, wake alive Minion and show YOU ARE Imp.",
            defaultReminders = listOf("Dead")
        ),

        // === BAD MOON RISING HIGHLIGHTS ===
        BotcCharacter(
            id = "grandmother",
            name = "Grandmother",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_BAD_MOON_RISING,
            ability = "You start knowing a good player & their character. If the Demon kills them, you die too.",
            firstNightOrder = 40,
            otherNightsOrder = 51,
            firstNightReminder = "Point to the Grandchild player and show their character token.",
            defaultReminders = listOf("Grandchild")
        ),
        BotcCharacter(
            id = "sailor",
            name = "Sailor",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_BAD_MOON_RISING,
            ability = "Each night, choose an alive player: either you or they are drunk until dusk. You cannot die.",
            firstNightOrder = 10,
            otherNightsOrder = 4,
            firstNightReminder = "Sailor chooses an alive player. Choose Sailor or chosen player to be Drunk.",
            otherNightsReminder = "Sailor chooses an alive player. Choose Sailor or chosen player to be Drunk.",
            defaultReminders = listOf("Drunk")
        ),
        BotcCharacter(
            id = "chambermaid",
            name = "Chambermaid",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_BAD_MOON_RISING,
            ability = "Each night, choose 2 alive players: you learn how many woke tonight due to their ability.",
            firstNightOrder = 50,
            otherNightsOrder = 69,
            firstNightReminder = "Chambermaid chooses 2 alive players. Show finger count of how many woke tonight.",
            otherNightsReminder = "Chambermaid chooses 2 alive players. Show finger count of how many woke tonight."
        ),
        BotcCharacter(
            id = "exorcist",
            name = "Exorcist",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_BAD_MOON_RISING,
            ability = "Each night*, choose a player (different to last night): if Demon, they learn who you are & don't wake tonight.",
            otherNightsOrder = 22,
            otherNightsReminder = "Exorcist chooses player. If Demon, Demon does not wake to kill tonight; wake Demon and show Exorcist token + point to Exorcist.",
            defaultReminders = listOf("Chosen")
        ),
        BotcCharacter(
            id = "innkeeper",
            name = "Innkeeper",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_BAD_MOON_RISING,
            ability = "Each night*, choose 2 players: they can't die tonight, but 1 is drunk until dusk.",
            otherNightsOrder = 9,
            otherNightsReminder = "Innkeeper chooses 2 players. Mark both SAFE; mark 1 as DRUNK.",
            defaultReminders = listOf("Safe", "Drunk")
        ),
        BotcCharacter(
            id = "gambler",
            name = "Gambler",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_BAD_MOON_RISING,
            ability = "Each night*, choose a player & guess their character: if you guess wrong, you die.",
            otherNightsOrder = 11,
            otherNightsReminder = "Gambler chooses player and character. If incorrect, mark Gambler DEAD.",
            defaultReminders = listOf("Dead")
        ),
        BotcCharacter(
            id = "gossip",
            name = "Gossip",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_BAD_MOON_RISING,
            ability = "Each day, you may make a public statement. Tonight, if it was true, a player dies.",
            otherNightsOrder = 38,
            otherNightsReminder = "If Gossip's public statement was true, choose a player to die (place DEAD).",
            defaultReminders = listOf("Dead")
        ),
        BotcCharacter(
            id = "courtier",
            name = "Courtier",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_BAD_MOON_RISING,
            ability = "Once per game, at night, choose a character: they are drunk for 3 nights & 3 days.",
            firstNightOrder = 20,
            otherNightsOrder = 10,
            firstNightReminder = "Courtier may choose a character. If chosen, mark drunk for 3 days.",
            otherNightsReminder = "Courtier may choose a character. If chosen, mark drunk for 3 days.",
            defaultReminders = listOf("Drunk 1", "Drunk 2", "Drunk 3", "No Ability")
        ),
        BotcCharacter(
            id = "professor",
            name = "Professor",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_BAD_MOON_RISING,
            ability = "Once per game, at night*, choose a dead player: if they are a Townsfolk, they are resurrected.",
            otherNightsOrder = 44,
            otherNightsReminder = "Professor may choose dead player. If Townsfolk, resurrect player (alive, retain vote).",
            defaultReminders = listOf("Alive", "No Ability")
        ),
        BotcCharacter(
            id = "minstrel",
            name = "Minstrel",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_BAD_MOON_RISING,
            ability = "When a Minion dies by execution, all other players (except Travellers) are drunk until dusk tomorrow.",
            defaultReminders = listOf("Everyone Drunk")
        ),
        BotcCharacter(
            id = "tealady",
            name = "Tea Lady",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_BAD_MOON_RISING,
            ability = "If both your alive neighbours are good, they cannot die.",
            defaultReminders = listOf("Can't Die")
        ),
        BotcCharacter(
            id = "pacifist",
            name = "Pacifist",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_BAD_MOON_RISING,
            ability = "Executed good players might not die."
        ),
        BotcCharacter(
            id = "fool",
            name = "Fool",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_BAD_MOON_RISING,
            ability = "The first time you die, you don't.",
            defaultReminders = listOf("No Ability")
        ),
        BotcCharacter(
            id = "goon",
            name = "Goon",
            type = CharacterType.OUTSIDER,
            edition = SCRIPT_BAD_MOON_RISING,
            ability = "Each night, the 1st player to choose you with their ability is drunk until dusk. You become their alignment.",
            firstNightOrder = 7,
            otherNightsOrder = 2,
            defaultReminders = listOf("Drunk")
        ),
        BotcCharacter(
            id = "lunatic",
            name = "Lunatic",
            type = CharacterType.OUTSIDER,
            edition = SCRIPT_BAD_MOON_RISING,
            ability = "You think you are a Demon, but you are not. The Demon knows who you are & who you choose at night.",
            firstNightOrder = 8,
            otherNightsOrder = 21,
            firstNightReminder = "Show Lunatic the Demon tokens & mock Minions. Wake Demon and show Lunatic.",
            defaultReminders = listOf("Attack 1", "Attack 2", "Attack 3")
        ),
        BotcCharacter(
            id = "tinker",
            name = "Tinker",
            type = CharacterType.OUTSIDER,
            edition = SCRIPT_BAD_MOON_RISING,
            ability = "You might die at any time.",
            defaultReminders = listOf("Dead")
        ),
        BotcCharacter(
            id = "moonchild",
            name = "Moonchild",
            type = CharacterType.OUTSIDER,
            edition = SCRIPT_BAD_MOON_RISING,
            ability = "When you learn that you died, publicly choose 1 alive player. Tonight, if it was a good player, they die.",
            otherNightsOrder = 49,
            defaultReminders = listOf("Dead")
        ),
        BotcCharacter(
            id = "godfather",
            name = "Godfather",
            type = CharacterType.MINION,
            edition = SCRIPT_BAD_MOON_RISING,
            ability = "You start knowing which Outsiders are in play. If an Outsider died today, choose a player tonight: they die. [-1 or +1 Outsider]",
            firstNightOrder = 21,
            otherNightsOrder = 37,
            firstNightReminder = "Show Godfather which Outsiders are in play.",
            otherNightsReminder = "If an Outsider died today, wake Godfather to choose a player to die.",
            defaultReminders = listOf("Dead")
        ),
        BotcCharacter(
            id = "devilsadvocate",
            name = "Devil's Advocate",
            type = CharacterType.MINION,
            edition = SCRIPT_BAD_MOON_RISING,
            ability = "Each night, choose an alive player (different to last night): if executed tomorrow, they don't die.",
            firstNightOrder = 22,
            otherNightsOrder = 14,
            firstNightReminder = "DA chooses player. Place SURVIVES EXECUTION.",
            otherNightsReminder = "DA chooses player. Place SURVIVES EXECUTION.",
            defaultReminders = listOf("Survives Execution")
        ),
        BotcCharacter(
            id = "assassin",
            name = "Assassin",
            type = CharacterType.MINION,
            edition = SCRIPT_BAD_MOON_RISING,
            ability = "Once per game, at night*, choose a player: they die, even if for some reason they could not.",
            otherNightsOrder = 36,
            otherNightsReminder = "Assassin may choose a player. That player dies (cannot be protected).",
            defaultReminders = listOf("Dead", "No Ability")
        ),
        BotcCharacter(
            id = "mastermind",
            name = "Mastermind",
            type = CharacterType.MINION,
            edition = SCRIPT_BAD_MOON_RISING,
            ability = "If the Demon dies by execution (ending the game), play for 1 more day. If a player is executed tomorrow, their team loses."
        ),
        BotcCharacter(
            id = "zombuul",
            name = "Zombuul",
            type = CharacterType.DEMON,
            edition = SCRIPT_BAD_MOON_RISING,
            ability = "Each night*, if no-one died today, choose a player: they die. The 1st time you die, you live but register as dead.",
            otherNightsOrder = 25,
            otherNightsReminder = "If no one died today, Zombuul chooses a player to die.",
            defaultReminders = listOf("Died Today", "Dead")
        ),
        BotcCharacter(
            id = "pukka",
            name = "Pukka",
            type = CharacterType.DEMON,
            edition = SCRIPT_BAD_MOON_RISING,
            ability = "Each night, choose a player: they are poisoned. The previously poisoned player dies then becomes healthy.",
            firstNightOrder = 28,
            otherNightsOrder = 26,
            firstNightReminder = "Pukka chooses a player to poison.",
            otherNightsReminder = "Pukka chooses player to poison. Previously poisoned player dies.",
            defaultReminders = listOf("Poisoned", "Dead")
        ),
        BotcCharacter(
            id = "shabaloth",
            name = "Shabaloth",
            type = CharacterType.DEMON,
            edition = SCRIPT_BAD_MOON_RISING,
            ability = "Each night*, choose 2 players: they die. A dead player you chose last night might be regurgitated.",
            otherNightsOrder = 27,
            otherNightsReminder = "Shabaloth chooses 2 players to die. ST may regurgitate (resurrect) 1 player killed last night.",
            defaultReminders = listOf("Dead", "Alive")
        ),
        BotcCharacter(
            id = "po",
            name = "Po",
            type = CharacterType.DEMON,
            edition = SCRIPT_BAD_MOON_RISING,
            ability = "Each night*, you may choose a player: they die. If your last choice was no-one, choose 3 players tonight.",
            otherNightsOrder = 28,
            otherNightsReminder = "Po chooses 1 player, or 0 players, or 3 players if chose 0 last night.",
            defaultReminders = listOf("Dead", "3 Attacks")
        ),

        // === SECTS & VIOLETS HIGHLIGHTS ===
        BotcCharacter(
            id = "clockmaker",
            name = "Clockmaker",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_SECTS_AND_VIOLETS,
            ability = "You start knowing how many steps from the Demon to its nearest Minion.",
            firstNightOrder = 41,
            firstNightReminder = "Show finger count for distance between Demon and closest Minion."
        ),
        BotcCharacter(
            id = "dreamer",
            name = "Dreamer",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_SECTS_AND_VIOLETS,
            ability = "Each night, choose a player (not yourself or Travellers): you learn 1 good and 1 evil character, 1 of which is correct.",
            firstNightOrder = 42,
            otherNightsOrder = 56,
            firstNightReminder = "Dreamer chooses a player. Show 1 good and 1 evil character token (one is correct).",
            otherNightsReminder = "Dreamer chooses a player. Show 1 good and 1 evil character token (one is correct)."
        ),
        BotcCharacter(
            id = "snakecharmer",
            name = "Snake Charmer",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_SECTS_AND_VIOLETS,
            ability = "Each night, choose an alive player: a chosen Demon swaps characters & alignments with you & is then poisoned.",
            firstNightOrder = 23,
            otherNightsOrder = 12,
            firstNightReminder = "Snake Charmer chooses player. If Demon, swap roles/alignments; new Snake Charmer is poisoned.",
            otherNightsReminder = "Snake Charmer chooses player. If Demon, swap roles/alignments; new Snake Charmer is poisoned.",
            defaultReminders = listOf("Poisoned")
        ),
        BotcCharacter(
            id = "mathematician",
            name = "Mathematician",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_SECTS_AND_VIOLETS,
            ability = "Each night, you learn how many players’ abilities worked abnormally (since dawn) due to another character's ability.",
            firstNightOrder = 51,
            otherNightsOrder = 70,
            firstNightReminder = "Show finger count of abilities that worked abnormally.",
            otherNightsReminder = "Show finger count of abilities that worked abnormally."
        ),
        BotcCharacter(
            id = "flowergirl",
            name = "Flowergirl",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_SECTS_AND_VIOLETS,
            ability = "Each night*, you learn if a Demon voted today.",
            otherNightsOrder = 57,
            otherNightsReminder = "Nod yes if Demon voted today, shake no if not.",
            defaultReminders = listOf("Demon Voted")
        ),
        BotcCharacter(
            id = "towncrier",
            name = "Town Crier",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_SECTS_AND_VIOLETS,
            ability = "Each night*, you learn if a Minion nominated today.",
            otherNightsOrder = 58,
            otherNightsReminder = "Nod yes if Minion nominated today, shake no if not.",
            defaultReminders = listOf("Minion Nominated")
        ),
        BotcCharacter(
            id = "oracle",
            name = "Oracle",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_SECTS_AND_VIOLETS,
            ability = "Each night*, you learn how many dead players are evil.",
            otherNightsOrder = 59,
            otherNightsReminder = "Show finger count of how many dead players are evil."
        ),
        BotcCharacter(
            id = "savant",
            name = "Savant",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_SECTS_AND_VIOLETS,
            ability = "Each day, you may visit the Storyteller to learn 2 things in private: 1 is true & 1 is false."
        ),
        BotcCharacter(
            id = "seamstress",
            name = "Seamstress",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_SECTS_AND_VIOLETS,
            ability = "Once per game, at night, choose 2 players: you learn if they are on the same team.",
            firstNightOrder = 43,
            otherNightsOrder = 60,
            firstNightReminder = "Seamstress may choose 2 players. Nod yes if same alignment, shake no if different.",
            otherNightsReminder = "Seamstress may choose 2 players. Nod yes if same alignment, shake no if different.",
            defaultReminders = listOf("No Ability")
        ),
        BotcCharacter(
            id = "philosopher",
            name = "Philosopher",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_SECTS_AND_VIOLETS,
            ability = "Once per game, at night, choose a good character: you gain that ability. If this character is in play, they are drunk.",
            firstNightOrder = 2,
            otherNightsOrder = 3,
            firstNightReminder = "Philosopher may choose a character token. If in play, mark original player DRUNK.",
            otherNightsReminder = "Philosopher may choose a character token. If in play, mark original player DRUNK.",
            defaultReminders = listOf("Drunk", "Is The Philosopher")
        ),
        BotcCharacter(
            id = "artist",
            name = "Artist",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_SECTS_AND_VIOLETS,
            ability = "Once per game, during the day, privately ask the Storyteller any yes/no question.",
            defaultReminders = listOf("No Ability")
        ),
        BotcCharacter(
            id = "juggler",
            name = "Juggler",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_SECTS_AND_VIOLETS,
            ability = "On your 1st day, publicly guess up to 5 players' characters. That night, you learn how many you got correct.",
            otherNightsOrder = 61,
            otherNightsReminder = "Show finger count of correct guesses from day 1.",
            defaultReminders = listOf("Correct Guesses")
        ),
        BotcCharacter(
            id = "sage",
            name = "Sage",
            type = CharacterType.TOWNSFOLK,
            edition = SCRIPT_SECTS_AND_VIOLETS,
            ability = "If the Demon kills you, you learn that it is 1 of 2 players.",
            otherNightsOrder = 43,
            otherNightsReminder = "If Demon killed Sage, wake Sage and point to 2 players (one is Demon)."
        ),
        BotcCharacter(
            id = "mutant",
            name = "Mutant",
            type = CharacterType.OUTSIDER,
            edition = SCRIPT_SECTS_AND_VIOLETS,
            ability = "If you are ”mad” about being an Outsider, you might be executed."
        ),
        BotcCharacter(
            id = "sweetheart",
            name = "Sweetheart",
            type = CharacterType.OUTSIDER,
            edition = SCRIPT_SECTS_AND_VIOLETS,
            ability = "When you die, 1 player is drunk from now on.",
            otherNightsOrder = 41,
            otherNightsReminder = "When Sweetheart dies, choose 1 player to be permanently drunk.",
            defaultReminders = listOf("Drunk")
        ),
        BotcCharacter(
            id = "barber",
            name = "Barber",
            type = CharacterType.OUTSIDER,
            edition = SCRIPT_SECTS_AND_VIOLETS,
            ability = "If you died today or tonight, the Demon may choose 2 players (not another Demon) to swap characters.",
            otherNightsOrder = 40,
            otherNightsReminder = "If Barber died, wake Demon to optionally choose 2 players to swap characters.",
            defaultReminders = listOf("Haircuts")
        ),
        BotcCharacter(
            id = "klutz",
            name = "Klutz",
            type = CharacterType.OUTSIDER,
            edition = SCRIPT_SECTS_AND_VIOLETS,
            ability = "When you learn that you died, publicly choose 1 alive player: if they are evil, your team loses."
        ),
        BotcCharacter(
            id = "eviltwin",
            name = "Evil Twin",
            type = CharacterType.MINION,
            edition = SCRIPT_SECTS_AND_VIOLETS,
            ability = "You & an opposing player know each other. If the good twin dies, evil wins. Good cannot win while both live.",
            firstNightOrder = 24,
            firstNightReminder = "Wake Evil Twin and Good Twin. Show each other. Place TWIN reminder.",
            defaultReminders = listOf("Twin")
        ),
        BotcCharacter(
            id = "witch",
            name = "Witch",
            type = CharacterType.MINION,
            edition = SCRIPT_SECTS_AND_VIOLETS,
            ability = "Each night, choose a player: if they nominate tomorrow, they die. If just 3 players live, you lose this ability.",
            firstNightOrder = 25,
            otherNightsOrder = 15,
            firstNightReminder = "Witch chooses a player. Place CURSED reminder.",
            otherNightsReminder = "Witch chooses a player. Place CURSED reminder.",
            defaultReminders = listOf("Cursed")
        ),
        BotcCharacter(
            id = "cerenovus",
            name = "Cerenovus",
            type = CharacterType.MINION,
            edition = SCRIPT_SECTS_AND_VIOLETS,
            ability = "Each night, choose a player & a good character: they are ”mad” they are this character tomorrow, or might be executed.",
            firstNightOrder = 26,
            otherNightsOrder = 16,
            firstNightReminder = "Cerenovus chooses player & good character. Wake chosen player, show MAD token & character token.",
            otherNightsReminder = "Cerenovus chooses player & good character. Wake chosen player, show MAD token & character token.",
            defaultReminders = listOf("Mad")
        ),
        BotcCharacter(
            id = "pithag",
            name = "Pit-Hag",
            type = CharacterType.MINION,
            edition = SCRIPT_SECTS_AND_VIOLETS,
            ability = "Each night*, choose a player & a character they become (if not-in-play). If a Demon is made, deaths tonight are arbitrary.",
            otherNightsOrder = 17,
            otherNightsReminder = "Pit-Hag chooses player and character. If valid, that player transforms into that character.",
            defaultReminders = listOf("Transformed")
        ),
        BotcCharacter(
            id = "fanggu",
            name = "Fang Gu",
            type = CharacterType.DEMON,
            edition = SCRIPT_SECTS_AND_VIOLETS,
            ability = "Each night*, choose a player: they die. The 1st Outsider this kills becomes an evil Fang Gu & you die instead. [+1 Outsider]",
            otherNightsOrder = 29,
            otherNightsReminder = "Fang Gu chooses player to kill. If 1st Outsider, that Outsider becomes evil Fang Gu; original Fang Gu dies.",
            defaultReminders = listOf("Dead", "Once")
        ),
        BotcCharacter(
            id = "vigormortis",
            name = "Vigormortis",
            type = CharacterType.DEMON,
            edition = SCRIPT_SECTS_AND_VIOLETS,
            ability = "Each night*, choose a player: they die. Minions you kill keep their ability & poison 1 Townsfolk neighbor. [-1 Outsider]",
            otherNightsOrder = 30,
            otherNightsReminder = "Vigormortis chooses player to kill. If Minion killed, Minion keeps ability and poisons 1 Townsfolk neighbor.",
            defaultReminders = listOf("Dead", "Has Ability", "Poisoned")
        ),
        BotcCharacter(
            id = "nodashii",
            name = "No Dashii",
            type = CharacterType.DEMON,
            edition = SCRIPT_SECTS_AND_VIOLETS,
            ability = "Each night*, choose a player: they die. Your 2 Townsfolk neighbours are poisoned.",
            otherNightsOrder = 31,
            otherNightsReminder = "No Dashii chooses a player to kill. Mark 2 Townsfolk neighbours as POISONED.",
            defaultReminders = listOf("Dead", "Poisoned")
        ),
        BotcCharacter(
            id = "vortox",
            name = "Vortox",
            type = CharacterType.DEMON,
            edition = SCRIPT_SECTS_AND_VIOLETS,
            ability = "Each night*, choose a player: they die. Townsfolk abilities yield false info. Each day, if no-one is executed, evil wins.",
            otherNightsOrder = 32,
            otherNightsReminder = "Vortox chooses a player to kill. All Townsfolk info tonight must be false.",
            defaultReminders = listOf("Dead", "False Info")
        ),

        // === POPULAR TRAVELERS & FABLED ===
        BotcCharacter(
            id = "beggar",
            name = "Beggar",
            type = CharacterType.TRAVELER,
            edition = "Travelers",
            ability = "You must use a vote token to vote. If a dead player gives you theirs, you learn their alignment. You are sober and healthy."
        ),
        BotcCharacter(
            id = "gunslinger",
            name = "Gunslinger",
            type = CharacterType.TRAVELER,
            edition = "Travelers",
            ability = "Each day, after the 1st vote has been tallied, you may choose a player that voted: they die."
        ),
        BotcCharacter(
            id = "thief",
            name = "Thief",
            type = CharacterType.TRAVELER,
            edition = "Travelers",
            ability = "Each night, choose a player (not yourself): their vote counts negatively tomorrow."
        ),
        BotcCharacter(
            id = "bureaucrat",
            name = "Bureaucrat",
            type = CharacterType.TRAVELER,
            edition = "Travelers",
            ability = "Each night, choose a player (not yourself): their vote counts as 3 votes tomorrow."
        ),
        BotcCharacter(
            id = "bonecollector",
            name = "Bone Collector",
            type = CharacterType.TRAVELER,
            edition = "Travelers",
            ability = "Once per game, at night, choose a dead player: they regain their ability until dusk."
        )
    )

    fun getCharactersForScript(script: String): List<BotcCharacter> {
        return ALL_CHARACTERS.filter { it.edition == script || it.type == CharacterType.TRAVELER || it.type == CharacterType.FABLED }
    }

    fun findCharacter(id: String?): BotcCharacter? {
        if (id == null) return null
        return ALL_CHARACTERS.find { it.id.equals(id, ignoreCase = true) }
    }

    val COMMON_REMINDERS = listOf(
        "Poisoned",
        "Drunk",
        "Protected",
        "Dead",
        "False Info",
        "Red Herring",
        "Master",
        "Mad",
        "Cursed",
        "Twin",
        "Nominated",
        "Voted",
        "Safe",
        "Used Once"
    )
}
