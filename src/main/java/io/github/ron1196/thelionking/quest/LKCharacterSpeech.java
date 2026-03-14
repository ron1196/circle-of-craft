package io.github.ron1196.thelionking.quest;

import java.util.Random;

public enum LKCharacterSpeech {
    MORNING_REPORT("Zazu", Speech.MORNING_REPORT),
    HYENA_BONES("Rafiki", Speech.HYENA_BONES),
    MENTION_SCAR("Rafiki", Speech.MENTION_SCAR),
    TERMITES("Rafiki", Speech.TERMITES),
    MANGOES("Rafiki", Speech.MANGOES),
    STAR_ALTAR("Rafiki", Speech.STAR_ALTAR),
    HINT("Rafiki", Speech.HINT),
    FLOWERS("Rafiki", Speech.FLOWERS),
    BUGS("Timon", Speech.BUGS),
    MORE_BUGS("Timon", Speech.MORE_BUGS),
    RUG_SCAR("Scar", Speech.RUG_SCAR),
    ZIRA_INGOTS("Zira", Speech.ZIRA_INGOTS),
    ZIRA_FEATHERS("Zira", Speech.ZIRA_FEATHERS),
    ZIRA_CONQUEST("Zira", Speech.ZIRA_CONQUEST),
    RUG_ZIRA("Zira", Speech.RUG_ZIRA),
    LION("Lion", Speech.LION),
    LIONESS("Lioness", Speech.LION),
    LION_CUB("Lion Cub", Speech.LION_CUB),
    LIONESS_CUB("Lioness Cub", Speech.LION_CUB),
    ZEBRA("Zebra", Speech.ZEBRA),
    ZEBRA_FOAL("Zebra Foal", Speech.ZEBRA_FOAL),
    RHINO("Rhino", Speech.RHINO),
    RHINO_CALF("Rhino Calf", Speech.RHINO_CALF),
    GEMSBOK("Gemsbok", Speech.GEMSBOK),
    GEMSBOK_CALF("Gemsbok Calf", Speech.GEMSBOK_CALF);

    private static final Random random = new Random();
    private final String characterName;
    private final String[] speeches;

    LKCharacterSpeech(String name, String[] speeches) {
        this.characterName = name;
        this.speeches = speeches;
    }

    public static String giveSpeech(LKCharacterSpeech speech) {
        return "§e<" + speech.characterName + "> §f" + speech.speeches[random.nextInt(speech.speeches.length)];
    }

    public String getCharacterName() {
        return characterName;
    }

    private static final class Speech {
        static final String[] MORNING_REPORT = {
                "The chimps are going ape!",
                "As usual, Giraffes remain above it all.",
                "Elephants remember. Though just what I can't recall...",
                "Crocodiles are snapping up new offers from the banks.",
                "We haven't paid the hornbills!",
                "The rabbits have been breeding by leaps and bounds.",
                "The lions are taking great pride in their families.",
                "Warthogs have been thwarted in attempts to save their gas.",
                "The buzz from the bees is that the leopards are in a bit of a spot.",
                "Cheetahs never prosper!",
                "Two silk worms had a race. They ended up in a tie.",
                "A snail's lost its shell. It's been very sluggish recently.",
                "Bears have been fighting again, but this time it's turned grizzly!",
                "The owls have been complaining again, but quite honestly, I don't give a hoot about them.",
                "Pigs are losing their voices! They seem very disgruntled.",
                "You can't trust a tiger. You never know when he could be lion...",
        };

        static final String[] HYENA_BONES = {
                "I need sixty-four hyena bones if you want one of my sticks.",
                "You don't have those hyena bones yet? A cub could do better!",
                "Bring me a stack of hyena bones if you want me to give you a stick.",
                "No stick for you until I get my hyena bones!",
                "Help rid the Pride Lands of hyenas first. Then you'll get your stick.",
                "If you want my stick, I need sixty-four hyena bones.",
                "What happened to that stack of hyena bones you promised me?",
                "Come back when you have the hyena bones."
        };

        static final String[] MENTION_SCAR = {
                "You need to kill Scar! He's a danger to us all!",
                "While Scar lives, the Pride Lands are in danger!",
                "May the great kings of the past watch over you.",
                "I think Scar is in a cave somewhere nearby...",
                "You should try looking underground for Scar.",
                "My stick is the only weapon which can harm Scar."
        };

        static final String[] TERMITES = {
                "I know the Outlands are dangerous, but we can't continue until you bring me four ground termites.",
                "I need four ground termites!",
                "Once I have four ground termites, we can carry on.",
                "You'll need to put the termites in a Grinding Bowl.",
                "I need ground termites for a reason, you know...",
                "Do you have those ground termites yet?",
                "Find some termites, put them in a Grinding Bowl, and bring them to old Rafiki."
        };

        static final String[] MANGOES = {
                "Bring me four ground mangoes!",
                "I only need four ground mangoes, then we can carry on!",
                "Hurry up with the mango dust!",
                "I want my ground mangoes.",
                "Have you been eating all the mangoes?",
                "You need to find four mangoes and put them all in the Grinding Bowl.",
                "You don't have the mangoes yet? It's not as if they grow on trees..."
        };

        static final String[] STAR_ALTAR = {
                "You need to craft a Star Altar with three Rafiki Dust and three silver ingots.",
                "Use some Rafiki Dust on the Star Altar!",
                "The Star Altar must be placed in view of the sky.",
                "Go and use the Star Altar."
        };

        static final String[] HINT = {
                "Are you taking good care of Simba?",
                "How is Simba doing? Is he dead yet?",
                "I hope you've been feeding Simba.",
                "Don't listen to me, I'm just a crazy old monkey.",
                "When Simba grows up, he can carry things for you!",
                "Why not go and kill some more hyenas? Pesky, slobbering, mangy creatures...",
                "Have you found Timon and Pumbaa yet?",
                "You are a baboon, and I am not. Ha!",
                "You had better be replanting all those mango trees!",
                "Go and play with Simba.",
                "I hear you can find chocolate treats in dungeons round here.",
                "Zebras are black, with white stripes.",
                "Did you know that my stick can harvest leaves?",
                "I wish there were some bananas nearby.",
                "I wasn't always this old, you know.",
                "Try taking Simba into some water. It can be a rewarding experience.",
                "Where have all the flowers gone?",
                "If you've lost your Pride Lands portal, you can look in the Book of Quests to find its location.",
                "Peacock Gems are useful for making very powerful tools.",
                "If a Rafiki Tree falls in the Pride Lands and no one is around, does it make a sound?",
                "I hear that angry lions will calm down again when fed Zebra Milk.",
                "You can take Simba through portals to other realms! Just craft an Astral Charm and give it to him.",
                "Lightning strikes in the Outlands form Outsand.",
                "You can recharge Voided Charms at the Star Altar.",
                "In Upendi... where the passion fruit grows sweet!",
                "I hear there are minerals in the Outlands with dark and dangerous powers.",
                "You smell of Pumbaa Flatulence...",
                "Roses are red, Zazus are blue. Some poems rhyme... and this one does too.",
                "Corrupt Pridestone makes some of the strongest tools, but they weaken very quickly.",
                "You should try enchanting my stick. The results can be thunderously good.",
                "Some of the termite mounds in the Outlands have treasure inside them!",
                "I haven't seen Ticket Lion in a while. He's an old friend of mine, you know.",
                "Don't even think about stealing any torches from my tree.",
                "Mango! Milk! A bug and a bowl! Stewing insects is my goal!",
                "Beware of speaking to Zira. You can't trust her!",
                "Be careful in the Outlands. There are dangerous and evil creatures there.",
                "Do you still have Scar's rug?",
                "Have you tried crafting some Peacock Wings yet?",
                "Giraffes have come to the Pride Lands! I love giraffes!",
                "What time does the narwhal bacon?"
        };

        static final String[] FLOWERS = {
                "What beautiful flowers!",
                "Those flowers are simply divine.",
                "Oh, I do like those flowers.",
                "The flowers are just wonderful.",
                "The flowers of the Pride Lands bring tears to my eyes!",
                "Such marvellous flowers you have there."
        };

        static final String[] BUGS = {
                "Do you have those bugs yet?",
                "We're really hungry! Please, bring us some bugs.",
                "If you bring us some nice juicy bugs, I might have some rewards for you.",
                "Stop staring at me. Bring me some bugs!",
                "There must be a fallen log somewhere around here!",
                "You can find bugs under fallen logs.",
                "Hurry up with those bugs. Pumbaa and I are starving!"
        };

        static final String[] MORE_BUGS = {
                "Those bugs were delicious. But there's always room for more!",
                "You want something else from me? Bring us some more bugs.",
                "We're still hungry, you know.",
                "Stop staring at me. Bring me more bugs!",
                "There's plenty of bugs left in the Pride Lands. Take the hint?",
                "Do you have any more bugs?",
                "I'm not full yet. Come back when you have some more bugs.",
                "Have you tried using a Bug Trap? You can get even more bugs that way!",
                "If you can't find any logs, try making a Bug Trap!",
                "Bugs don't just hide under logs. You can lure them out into the open with a trap and the right bait."
        };

        static final String[] RUG_SCAR = {
                "How dare you treat me like this?",
                "Life's just not fair.",
                "Now I shall never be king.",
                "No! Stop treading on me!",
                "If I admit to killing Mufasa, will you let me go?",
                "Minions! Obliterate this scum!",
                "Where are my minions?",
                "Who did this to me? My minions shall tear their flesh apart!",
                "I should have snapped that Rafiki Stick while I had the chance.",
                "You will pay for this mockery!",
                "Where am I? Who are you? I despise guessing games.",
                "It's the hyenas who are the real enemy. It was their fault. It was their idea!",
                "Why did I rely on those idiotic hyenas?",
                "As far as brains go, I got the lion's share. You seem to be at the shallow end of the gene pool.",
                "I've got a lovely bunch of coconuts. There they are, all standing in a row...",
                "I strongly dislike being a rug.",
                "Alas, it seems I shall be occupying pride of place in someone's living room for the rest of my days.",
                "I feel quite rugged right now.",
                "Zazu always did say I'd make a very handsome throw rug."
        };

        static final String[] ZIRA_INGOTS = {
                "Bring me five kivulite and two silver ingots.",
                "I tire of waiting for the ingots.",
                "Need I remind you how hungry these Outlanders are?",
                "What? You don't have the silver and kivulite yet?",
                "You are most incompetent. Perhaps we should have attacked you after all.",
                "Surely it doesn't take this long to collect a few ingots?",
                "Haven't you found any kivulite yet? There's probably some in the caves nearby."
        };

        static final String[] ZIRA_FEATHERS = {
                "You don't have the feathers yet? I thought better of you.",
                "I need three Wayward Feathers!",
                "To get a Wayward Feather, you have to give the Outwater blue, yellow, red and black feathers.",
                "Go and kill some vultures. I never liked those birds.",
                "No, of course I'm not going to tell you what I need the feathers for.",
                "You get black feathers from vultures, and the other types from those infuriating Zazus in the Pride Lands.",
                "The Wayward Feathers aren't going to collect themselves, are they?"
        };

        static final String[] ZIRA_CONQUEST = {
                "This tree looks much nicer now it's under our control.",
                "The Pride Lands will be mine soon!",
                "We couldn't have got here without your help. Scar would have been proud of you.",
                "I'll never see a single vulture again!",
                "Have you tried throwing some more things into the Outwater? Of course, I wouldn't go back there, but it's worth experimenting with that strange pool.",
                "I wonder what Rafiki thought Timon and Pumbaa could possibly do against me?",
                "We will take this entire kingdom... by force!"
        };

        static final String[] RUG_ZIRA = {
                "Well, I never thought I'd end up like this.",
                "This is very uncomfortable.",
                "My Outlanders will exact a terrible revenge on you.",
                "I now see the path to our glorious return to power!",
                "Scar is gone, but Zira's still around...",
                "Outlanders! Annihilate this fool!",
                "Keep poking me like that and I shall bring a swift end to your pitiful existence!",
                "You have turned me into an ornament? How dare you!",
                "A plague of vultures shall avenge my death!"
        };

        static final String[] LION = {
                "By Mufasa's mane! You can understand me!",
                "I like to chew on hyena bones before I breed.",
                "Hyenas. Filthy, slobbering, mangy creatures...",
                "It's a wonderful day in the Pride Lands today.",
                "I haven't seen any hyenas recently. Have you?",
                "I've applied to be a Ticket Lion. They haven't responded yet.",
                "And how are you today, human?",
                "What do you call a lion running a copying machine? A copycat!",
                "Simba was moving too slowly, so I told him to Mufasa."
        };

        static final String[] LION_CUB = {"Roar!", "Grrr!", "Rrr!"};

        static final String[] ZEBRA = {
                "Stay away from the rainforests at night. That's when the crocodiles come out.",
                "I nearly got eaten by a crocodile! Good thing I can run faster than them.",
                "You can collect Zebra Milk in a jar and use it to calm down angry lions!",
                "Am I white with black stripes, or black with white stripes?",
                "My hide looks simply fabulous, don't you think?",
                "Aren't you the one that made my friends into boots?",
                "Zebra meat tastes disgusting, I'll have you know."
        };

        static final String[] ZEBRA_FOAL = {"Hmmph!", "Hssh!", "Hmm!"};

        static final String[] RHINO = {
                "Have I seen you before?",
                "Zebra Chops are much nicer than rhino meat.",
                "I didn't know you creatures could talk.",
                "Do you have any wheat?",
                "How do you stop a rhino charging? I wouldn't know, I don't seem to be able to charge.",
                "My full name is Rhinoceros.",
                "I've got one tail, four feet and twelve toes."
        };

        static final String[] RHINO_CALF = {"Rrr!", "Hmm!", "Hrrh!"};

        static final String[] GEMSBOK = {
                "I bet you've never even heard of a Gemsbok before.",
                "Don't you wish you had horns like mine?",
                "Gemsbok? How do you pronounce that?",
                "The grass around here is very nice. You should try some.",
                "I find the idea of a Gemsbok Spear very disturbing.",
                "What do you mean, I sound exactly like a zebra?",
                "Hello! I like wheat and corn!",
                "Stay away from the crocodiles!"
        };

        static final String[] GEMSBOK_CALF = {
                "Hmmph!",
                "Hssh!",
                "Hmm!"
        };
    }
}
