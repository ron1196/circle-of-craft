package io.github.ron1196.circleofcraft.quest;

import io.github.ron1196.circleofcraft.util.ChatHelper;
import java.util.Random;
import net.minecraft.world.entity.player.Player;

public enum CharacterSpeech {
    MORNING_REPORT("Zazu", Speech.MORNING_REPORT),
    ZAZU_SLEEPING("Zazu", Speech.ZAZU_SLEEPING),
    CRAFT_STICK("Rafiki", Speech.CRAFT_STICK),
    FIND_PUMBAA("Rafiki", Speech.FIND_PUMBAA),
    HYENA_BONES("Rafiki", Speech.HYENA_BONES),
    MENTION_SCAR("Rafiki", Speech.MENTION_SCAR),
    TERMITES("Rafiki", Speech.TERMITES),
    MANGOES("Rafiki", Speech.MANGOES),
    STAR_ALTAR("Rafiki", Speech.STAR_ALTAR),
    HINT("Rafiki", Speech.HINT),
    COMPLETE_HINT("Rafiki", Speech.COMPLETE_HINT),
    RAFIKI_COIN_SOLD("Rafiki", Speech.RAFIKI_COIN_SOLD),
    RAFIKI_BOOK_RESOLD("Rafiki", Speech.RAFIKI_BOOK_RESOLD),
    RAFIKI_EXTRA_STICK("Rafiki", Speech.RAFIKI_EXTRA_STICK),
    RAFIKI_DUST_MADE("Rafiki", Speech.RAFIKI_DUST_MADE),
    FLOWERS("Rafiki", Speech.FLOWERS),
    ASK_FOR_FLOWERS("Rafiki", Speech.ASK_FOR_FLOWERS),
    PUMBAA_NEED_BUGS("Pumbaa", Speech.PUMBAA_NEED_BUGS),
    TIMON_WAITING_BUGS("Timon", Speech.TIMON_WAITING_BUGS),
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

    CharacterSpeech(String name, String[] speeches) {
        this.characterName = name;
        this.speeches = speeches;
    }

    public static String giveSpeech(CharacterSpeech speech) {
        return ChatHelper.formatNpcMessage(
                speech.characterName, speech.speeches[random.nextInt(speech.speeches.length)]);
    }

    public static void sendSpeech(Player player, CharacterSpeech speech) {
        ChatHelper.sendNpcMessage(
                player, speech.characterName, speech.speeches[random.nextInt(speech.speeches.length)]);
    }

    private static final class Speech {
        static final String[] ZAZU_SLEEPING = {
            "Do you have ANY idea what time it is?! The Morning Report isn't until sunrise! Shoo!",
            "I beg your pardon! A royal majordomo requires his beauty rest. Come back at dawn, if you please!",
            "Good heavens! This is most irregular! My report is scheduled for sunrise — not a moment before!",
            "I am OFF duty! Even I deserve a few hours of peace from your insufferable pestering!",
            "Checking in at THIS hour? Mufasa never kept such uncivilized hours! Come back at first light!",
            "I'm a hornbill, not an owl! Now begone — and if you wake me again before sunrise, there'll be a very STERN memo!",
            "The audacity! Even when I was stuck in that bone cage, at least Scar let me SLEEP!"
        };

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

        static final String[] FIND_PUMBAA = {
            "Go and find Pumbaa! You cannot face Scar alone, oh no no no!",
            "Timon and Pumbaa are out dere somewhere. Find dem! Dey are de friends you need!",
            "You need allies for de fight ahead. Find Pumbaa! He is... formidable in his own special way! Hahaha!",
            "Pumbaa is a mighty warrior... well, mighty in spirit! And smell! Go find him!",
            "Have you found Timon and Pumbaa yet? Dey can help you! Trust old Rafiki on dis!",
            "Look around! Timon and Pumbaa are never far from each other. Where dere is a bad smell, dere is Pumbaa!"
        };

        static final String[] CRAFT_STICK = {
            "Bring old Rafiki a stick, a mango, and a bug! Hehe, den I make you something very special!",
            "Mangoes! Dey fall from de mango leaves — just give de tree a good shake!",
            "Bugs hide under fallen logs. Flip dem over! Dey don't bite... much! Hahaha!",
            "Have you tried making a Bug Trap? De bugs walk right in! Dey are not de brightest creatures!",
            "A stick, a mango, and a bug — dat is all old Rafiki needs!",
            "Look for mango trees! De fruit is sweet, and de dust is powerful!",
            "Bring me dose materials and I will craft you a stick of great power! Ohohoho!"
        };

        static final String[] HYENA_BONES = {
            "Sixty-four hyena bones! Dat is what old Rafiki needs to cleanse de Pride Lands!",
            "You don't have dose hyena bones yet? Even a baboon could do better! Hahaha!",
            "Bring me a full stack of hyena bones. De Pride Lands need you to be strong!",
            "No bones, no progress! Dat is how it works! Now chop chop!",
            "Rid de Pride Lands of dose slobbering, mangy creatures and bring me deir bones!",
            "I need sixty-four hyena bones. Not sixty-three! Not sixty-two! Sixty-four! Hehe!",
            "What happened to dat stack of hyena bones you promised old Rafiki, hmm?",
            "Come back when you have de bones. De spirits are waiting... and so am I!",
            "If de trip back here grows tiresome, bring me tree silver ingots — I will give you a coin to find your way home! Hehe!",
            "Have you lost your Quest Book? A book and a tuft of lion fur — bring dem to old Rafiki and I make you a new one!"
        };

        static final String[] MENTION_SCAR = {
            "You must find Scar! While he lives, de Pride Lands wither and die!",
            "Scar is out dere, hiding like de coward he is. De hyenas know where!",
            "May de great kings of de past watch over you in dis battle!",
            "I tink Scar is hiding underground somewhere... follow de hyenas! Dey always lead back to deir master!",
            "Hold my stick — it will guide you to Scar! Can you feel it pulling? Dat is de ancestors showing de way!",
            "My stick is de ONLY weapon dat can harm Scar. Remember dat! De spirits of de past made it so!"
        };

        static final String[] TERMITES = {
            "De Outlands are dangerous, yes yes, but we cannot continue until you bring me four termite dust!",
            "Four termite dust! Chop chop! De spirits grow impatient, and so does old Rafiki!",
            "Once I have four termite dust, we carry on. De ancestors have spoken!",
            "You must grind de termites in a Grinding Bowl. De dust has great power!",
            "I need dat termite dust for a VERY important reason, you know! Trust old Rafiki!",
            "Do you have de termite dust yet? Time is wasting!",
            "Find some termites, put dem in a Grinding Bowl, and bring de dust to old Rafiki. It is not so hard, yes?"
        };

        static final String[] MANGOES = {
            "Bring old Rafiki four mango dust! De recipe demands it!",
            "I only need four mango dust, den we can carry on! De spirits are almost ready!",
            "Hurry up wit de mango dust! Even de baboons are getting impatient!",
            "I want my mango dust! Old Rafiki has big plans, hehe!",
            "Have you been eating all de mangoes instead of grinding dem? I know dat look!",
            "Four mangoes — put dem all in de Grinding Bowl and bring me de dust!",
            "You don't have de mangoes yet? Dey grow on TREES, you know! Right above your head! Hahaha!"
        };

        static final String[] STAR_ALTAR = {
            "Now craft a Star Altar wit three Rafiki Dust and three silver ingots! De ancestors await!",
            "Use de Rafiki Dust on de Star Altar! Can you hear dem? De spirits are calling!",
            "De Star Altar must be placed under de open sky! De great kings must be able to see it!",
            "Go and use de Star Altar! De spirits of de past are watching! Ohohoho!"
        };

        static final String[] RAFIKI_COIN_SOLD = {
            "Ahh, silver! Here, take dis coin. Clutch it close and de coin will whisk you back to old Rafiki! Ohohoho!",
            "Tree silver pieces, perfect! Here is your coin. Hold it tight in your hand and it will guide you home! Hehe!",
            "Silver is good, but de spirits prefer dis form. Take dis coin — squeeze it gently and you will return to me!",
            "Wonderful, wonderful! De coin is yours. Cup it in your palms and de ancestors will carry you back! Ohohoho!",
            "Tank you for de silver! Now keep dis coin safe — clench it close to your heart and it will bring you home to old Rafiki!"
        };

        static final String[] RAFIKI_BOOK_RESOLD = {
            "You lost your Quest Book? Hehe! Silly creature! Here, take another one!",
            "Lost again?! How does one lose a book de size of a watermelon? Hehe! Here, take dis new one!",
            "Anudder Quest Book for de forgetful one! Try not to lose dis one too, yes?",
            "Ohohoho! Old Rafiki keeps spare Quest Books for situations like dis! Here you go!"
        };

        static final String[] RAFIKI_EXTRA_STICK = {
            "More bones! Old Rafiki can always use more bones! Here is another stick for you!",
            "Sixty-four hyena bones! Ohohoho! Dis is a fine offering. Take dis stick!",
            "You keep bringing me bones and I keep making you sticks. Dis is de circle of trade! Hehe!",
            "Anudder stick for anudder pile of bones — old Rafiki is always happy to oblige!"
        };

        static final String[] RAFIKI_DUST_MADE = {
            "More ingredients! Dis old baboon never tires of making dust! Hehe!",
            "Termite and mango — two halves of one powerful dust! Take it and use it wisely!",
            "Ohohoho! Anudder pinch of Rafiki Dust for you. De spirits dance whenever I make dis!",
            "Dust upon dust upon dust! Old Rafiki is a master craftsman, yes? Hehe!"
        };

        static final String[] HINT = {
            "Don't listen to me! I'm just a crazy old monkey! Hahaha!",
            "Why not go and smack some more hyenas? Pesky, slobbering, mangy creatures...",
            "You are a baboon, and I am not! Haha! Or... wait... maybe it is de other way around?",
            "I hear you can find chocolate treats in de dungeons round here. Even Rafiki likes chocolate!",
            "Zebras are black wit white stripes. Or is it white wit black stripes? Hmm... dis is de great mystery!",
            "Did you know dat my stick can harvest leaves? It can do many tings! Ohohoho!",
            "I wish dere were some bananas nearby. A baboon cannot live on mangoes alone!",
            "I wasn't always dis old, you know. Once I was young and foolish! Now I am old and foolish! Hahaha!",
            "Where have all de flowers gone? De Pride Lands need more beauty!",
            "If a Rafiki Tree falls in de Pride Lands and no one is around... does it land on a hyena? One can only hope!",
            "Roses are red, Zazus are blue. Some poems rhyme... and dis one does too! Hehe!",
            "I haven't seen Ticket Lion in a while. He is an old friend of mine, you know!",
            "Don't even TINK about stealing any torches from my tree! I know where you live!",
            "Giraffes have come to de Pride Lands! Dey make old Rafiki feel very short! Hahaha!",
            "What time does de narwhal bacon? Ohohoho! I don't even know what dat means!",
            "Asante sana, squash banana, wewe nugu, mimi hapana!",
            "Ah yes, de past can hurt. But de way I see it, you can either run from it... or learn from it!",
            "It does not matter! It is in de past! Hahaha!",
            "De question is... who are YOU?",
            "Change is good. But it is not easy. Hehe, dat is what makes it interesting!",
            "Oh ho ho ho! You follow old Rafiki — he knows de way!",
            "De circle of life moves us all, through despair and hope, through faith and love!",
            "Peacock Gems are useful for making very powerful tools. Even old Rafiki is impressed!"
        };

        static final String[] COMPLETE_HINT = {
            "Are you taking good care of little Simba? He needs a strong guardian!",
            "How is Simba doing? Is he dead yet? Hehe! I am only joking, of course!",
            "I hope you have been feeding Simba. A hungry lion is a cranky lion!",
            "When Simba grows up, he can carry tings for you! Patience, patience!",
            "Go and play wit Simba! He is still young, but he has de heart of a king!",
            "You had better be replanting all dose mango trees! De circle of life demands it!",
            "Have you found Timon and Pumbaa yet? Dey are... entertaining, to say de least!",
            "Try taking Simba into some water. It can be a... rewarding experience! Hehe!",
            "If you have lost your Pride Lands portal, look in de Book of Quests to find its location!",
            "I hear dat angry lions will calm down again when fed Zebra Milk. Who knew?",
            "You can take Simba through portals to other realms! Just craft an Astral Charm and give it to him!",
            "Lightning strikes in de Outlands form Outsand. De Outlands are full of surprises!",
            "You can recharge Voided Charms at de Star Altar. De ancestors are generous!",
            "In Upendi... where de passion fruit grows sweet! Hahaha!",
            "I hear dere are minerals in de Outlands wit dark and dangerous powers. Be careful!",
            "You smell of Pumbaa Flatulence... phew! Even old Rafiki's eyes are watering!",
            "Corrupt Pridestone makes some of de strongest tools, but dey weaken quickly. Like a hyena's resolve!",
            "You should try enchanting my stick! De results can be thunderously good! Ohohoho!",
            "Some of de termite mounds in de Outlands have treasure inside dem! Just watch out for de queen!",
            "Mango! Milk! A bug and a bowl! Stewing insects is old Rafiki's goal! Hehe!",
            "Beware of speaking to Zira. You cannot trust her! Not one bit!",
            "Be careful in de Outlands. Dere are dangerous and evil creatures lurking everywhere!",
            "Do you still have Scar's rug? Hehe! He makes a very handsome floor decoration!",
            "Have you tried crafting some Peacock Wings yet? De view from up dere is magnificent!",
            "Remember who you are! You are more dan what you have become!",
            "Look harder... he lives in you!",
            "De king... has returned.",
            "If you ever need a quick way home, bring me tree silver ingots — old Rafiki will trade you a coin to whisk you back!",
            "Got too many hyena bones rattling in your pack? Bring sixty-four to old Rafiki and I will make you another stick! Ohohoho!",
            "Two dusts make one Rafiki Dust — termite plus mango! Just bring dem both to me, easy peasy!",
            "Lost your Quest Book again? Tsk! Bring me a book and some lion fur and I will make you another one. Try not to lose dis one too! Hehe!"
        };

        static final String[] FLOWERS = {
            "Ohohoho! De flowers of de Pride Lands! Dey make old Rafiki's heart sing!",
            "Such beautiful flowers! De circle of life is in full bloom today!",
            "Ahh, dose flowers! Dey remind me of when de Pride Lands were young and green!",
            "Look at dose flowers! Even de baboons stop to admire dem! Hahaha!",
            "De flowers! Dey bring tears to dis old monkey's eyes! So beautiful!",
            "What marvellous flowers you have dere! De spirits of de ancestors smile upon dem!",
            "Dose vases! So full of color! Old Rafiki is most pleased! Hehe!",
            "You have brought flowers to old Rafiki's tree! De ancestors are dancing! Ohohoho!"
        };

        static final String[] ASK_FOR_FLOWERS = {
            "Where have all de flowers gone? De Pride Lands need more beauty!",
            "Bring old Rafiki some flowers in a vase, hmm? It would make dis old monkey very happy!",
            "Dis place could use some color! Pick some flowers and put dem in a vase nearby — old Rafiki loves de pretty tings!",
            "Have you seen any flowers in your travels? Old Rafiki misses dem! A vase full of blooms would be wonderful!",
            "De Pride Lands have many flowers — white, blue, red, purple! Bring some to old Rafiki's tree in a vase, yes?",
            "No flowers nearby? Tsk tsk! Even de baboons appreciate a nice bouquet! Hehe!"
        };

        static final String[] TIMON_WAITING_BUGS = {
            "Hey, you got those bugs for Pumbaa yet? We're dyin' over here!",
            "Pumbaa's still waitin' on those bugs, pal. And trust me, you do NOT want to hear his stomach growl.",
            "Four bugs! That's it! I've seen ants carry more than that!",
            "Come on, kid! Pumbaa's stomach is growling louder than Mufasa on a bad day!",
            "You know where to find bugs, right? Under logs! It ain't exactly rocket science!",
            "Pumbaa can't fight on an empty stomach. Believe me, I've tried makin' him. It ain't pretty."
        };

        static final String[] PUMBAA_NEED_BUGS = {
            "I'm gonna need at least four bugs before I can fight! A warthog's gotta eat, you know!",
            "Four bugs! That's all I'm askin' for. Hakuna Matata doesn't work on an empty tummy!",
            "If you want me fightin' fit, I need four nice, juicy bugs! Slimy yet satisfying!",
            "You call that enough bugs? I need four of 'em! I'm a growing warthog!",
            "I know it's gross to some folks, but I really do need those four bugs. It's a... dietary thing.",
            "I can't fight Scar on an empty belly! Four bugs, please! Even Timon agrees, and he NEVER agrees with me!"
        };

        static final String[] BUGS = {
            "Hey, you got those bugs yet? Pumbaa and me, we're wastin' away here!",
            "Listen, pal, bring us some nice juicy bugs and maybe — MAYBE — I'll have a reward for ya.",
            "Bugs! Grubs! Anything slimy and crunchy! We ain't picky!",
            "What are you starin' at? Go flip over some logs and bring me some bugs already!",
            "There's gotta be a fallen log around here somewhere. Bugs love those things!",
            "You can find bugs under fallen logs. It's the circle of life, kid — they eat the wood, we eat them!",
            "Hurry up with those bugs! Pumbaa's stomach sounds like a thunderstorm and I can't take it anymore!"
        };

        static final String[] MORE_BUGS = {
            "Those bugs were delicious! But let me tell ya, there's ALWAYS room for more.",
            "You want somethin' else from me? That'll cost ya. Bring us more bugs!",
            "We're still hungry, pal. Hakuna Matata means no worries, but no bugs? THAT I worry about.",
            "Quit starin' and start huntin'! More bugs, chop chop!",
            "There's plenty of bugs left in the Pride Lands. Take the hint, kid!",
            "Got any more of those crunchy critters? I'm askin' for a friend. The friend is Pumbaa. And also me.",
            "I ain't full yet. Come back when you got more bugs. And make 'em juicy!",
            "Hey, you tried using a Bug Trap? You can get WAY more bugs that way! Work smarter, not harder!",
            "If you can't find any logs, try makin' a Bug Trap! Even I could figure that one out.",
            "Bugs don't just hide under logs, ya know. Lure 'em out with a trap and the right bait. Get creative!"
        };

        static final String[] RUG_SCAR = {
            "Life's not fair, is it? You see, I shall never be king. And YOU shall never stop walking on me.",
            "How DARE you treat me — the rightful king — like some common doormat!",
            "I'm surrounded by idiots... and now I'm lying beneath one.",
            "I should have been king! Instead I'm a floor furnishing. The IRONY is not lost on me.",
            "I admit I killed Mufasa. Happy? Now kindly remove your feet from my face.",
            "Oh, SHENZI! BANZAI! ED! ...Where ARE my minions when I need them?",
            "That Rafiki Stick... I should have snapped it in two when I had the chance.",
            "Long live the king, they said. Well. Here I am. Very much alive. Very much... flat.",
            "Who did this to me? When I get out of here — and I WILL — there shall be a reckoning.",
            "It was the hyenas who are the real enemy! It was their fault! It was their idea!",
            "Why did I ever rely on those idiotic hyenas? At least I have a brain. Had. HAVE.",
            "As far as brains go, I got the lion's share. But as far as bodies go... I seem to have lost mine.",
            "I've got a lovely bunch of coconuts... deedle-dee-dee... there they are, all standing in a row...",
            "I am not a rug. I am a KING. A king who happens to be... temporarily horizontal.",
            "Zazu always said I'd make a very handsome throw rug. I suppose he got the last laugh after all.",
            "Be prepared! For the day I rise from this floor, your world will tremble!",
            "I feel quite... rugged. Is that what passes for humor around here?",
            "You will pay for this. Oh yes. Scar ALWAYS gets his revenge. Eventually. When he's not a rug."
        };

        static final String[] ZIRA_INGOTS = {
            "Five kivulite and two silver ingots. Bring them to me. NOW.",
            "I tire of waiting! Do you think Scar would have tolerated such incompetence?",
            "Need I remind you how hungry my Outlanders are? They haven't eaten in DAYS. Don't make me unleash them.",
            "You don't have the silver and kivulite yet? Perhaps I chose the wrong ally.",
            "You test my patience. Scar was right — you can never trust anyone but family.",
            "Surely it doesn't take this long to collect a few ingots? Even Nuka could do better!",
            "There's kivulite in the caves nearby. Find it, or find yourself at the mercy of my claws."
        };

        static final String[] ZIRA_FEATHERS = {
            "You don't have the feathers yet? I thought better of you. Don't make me reconsider.",
            "I need three Wayward Feathers! Scar's plan demands it!",
            "Throw colored feathers into the Outwater — blue, yellow, red, and black. The pool does the rest.",
            "Go and slaughter some vultures. I never liked those wretched birds.",
            "What I need the feathers for is NONE of your concern. Just bring them!",
            "Black feathers from vultures. The rest from those infuriating Zazus in the Pride Lands. Is that so difficult?",
            "The Wayward Feathers aren't going to collect themselves! Must I do EVERYTHING?"
        };

        static final String[] ZIRA_CONQUEST = {
            "This tree is MINE now! It looks much better under Outlander rule, don't you think?",
            "The Pride Lands will be mine! It is what Scar would have wanted! His vision... REALIZED!",
            "We couldn't have conquered this without you. Scar would have been proud. I, however, am merely satisfied.",
            "I shall never see another wretched vulture again! The skies belong to US now!",
            "Try throwing more things into the Outwater. I wouldn't go back there myself... but you might find it illuminating.",
            "Rafiki thought Timon and Pumbaa could stop ME? A meerkat and a warthog against the Outsiders? Laughable!",
            "We will take this ENTIRE kingdom... by FORCE if necessary! Scar's dream lives on!"
        };

        static final String[] RUG_ZIRA = {
            "I never thought I'd end up like this. Scar... I have failed you.",
            "This is most... uncomfortable. But a true warrior endures!",
            "My Outlanders will avenge me! You have NOT seen the last of Zira!",
            "Even from this floor, I can see the path to our glorious return to power!",
            "Scar is gone, but his legacy lives on through ME! Even as... this.",
            "Outlanders! RISE UP! Annihilate this fool who dares to walk upon me!",
            "Keep poking me like that and I shall find a way to bring a swift end to your pitiful existence!",
            "You have turned ME — the chosen one of Scar — into an ORNAMENT?!",
            "A plague of vultures shall descend upon you! Mark my words! MARK THEM!"
        };

        static final String[] LION = {
            "By Mufasa's mane! You... you can understand me?",
            "The Pride Lands are at peace today. As they should be.",
            "Hyenas... filthy, slobbering, mangy, stupid creatures.",
            "It is a fine day in the Pride Lands. The Circle of Life turns on.",
            "I haven't seen any hyenas recently. Good riddance, I say.",
            "I've put in my application to be a Ticket Lion. Still waiting to hear back. The bureaucracy is terrible.",
            "Greetings, human. The Pride Lands welcome you.",
            "What do you call a lion running a copying machine? A copycat! ...Simba told me that one.",
            "Simba was moving too slowly, so I told him to Mufasa! Hah!"
        };

        static final String[] LION_CUB = {"Roar!", "Grrr!", "Rrr!"};

        static final String[] ZEBRA = {
            "Stay away from the rainforests at night! That's when the crocodiles come out! Trust me, I KNOW!",
            "I nearly got eaten by a crocodile once! Good thing we zebras can run like the wind!",
            "You can collect Zebra Milk in a jar and use it to calm down angry lions! It's our... contribution.",
            "Am I white with black stripes, or black with white stripes? Even I don't know!",
            "My hide looks simply fabulous, don't you think? Don't answer that. I know it does.",
            "Aren't you the one who made my friends into boots? I've got my eye on you...",
            "Zebra meat tastes disgusting! I'm told. By other zebras. Who would know."
        };

        static final String[] ZEBRA_FOAL = {"Hmmph!", "Hssh!", "Hmm!"};

        static final String[] RHINO = {
            "Hmm. Have I seen you before? All you humans look the same to me.",
            "Zebra Chops taste way better than rhino meat. Not that I'd know. Just... spread the word.",
            "I didn't know you creatures could talk. Fascinating. Now leave me alone.",
            "Do you have any wheat? I'm asking nicely. I won't ask nicely twice.",
            "How do you stop a rhino from charging? I don't know either. Never tried stopping.",
            "My full name is Rhinoceros. But my friends call me Rhino. You can call me Rhinoceros.",
            "One tail, four feet, twelve toes, and one very sharp horn. Don't forget the horn."
        };

        static final String[] RHINO_CALF = {"Rrr!", "Hmm!", "Hrrh!"};

        static final String[] GEMSBOK = {
            "I bet you've never even HEARD of a gemsbok before, have you? We're rare!",
            "Don't you wish you had horns like mine? Of course you do. Everyone does.",
            "Gemsbok. G-E-M-S-B-O-K. Even I have trouble spelling it sometimes.",
            "The grass around here is superb. You should try some. No? Your loss.",
            "A Gemsbok Spear? Made from MY horns? I find that DEEPLY offensive.",
            "What do you mean, I sound exactly like a zebra? How DARE you! We sound nothing alike!",
            "Hello there! I enjoy wheat and corn! Simple pleasures for a sophisticated creature!",
            "Stay away from the crocodiles! I may be fast, but I'd rather not test it!"
        };

        static final String[] GEMSBOK_CALF = {"Hmmph!", "Hssh!", "Hmm!"};
    }
}
