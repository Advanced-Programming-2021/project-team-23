package Controllers.action;

import Controllers.CardController;
import Controllers.GameController;
import Models.Board;
import Models.Card;
import Views.GameView;

public abstract class Action {

    static Board myBoard;
    static Board opponentBoard;
    static int myNumber; // 0 or 1

    public static void setBoards(GameController gameController, Card myCard){
        Board board = gameController.getBoard(0);
        if(CardController.boardContainsCard(board, myCard)){
            myNumber = 0;
            myBoard = gameController.getBoard(0);
            opponentBoard = gameController.getBoard(1);
        } else {
            myNumber = 1;
            myBoard = gameController.getBoard(1);
            opponentBoard = gameController.getBoard(0);
        }
    }

    // call this if opponentCard == null:
    public abstract void runFirstAction(GameController gameController, Card myCard, Card opponentCard);

    // call this if opponentCard != null:
    public abstract void runActionForDefense(GameController gameController, Card myCard, Card opponentCard);

    // check this for cards on board before activation and adding to chain :
    public abstract boolean canEffectBeActivated(GameController gameController, Card myCard, Card opponentCard);


    public static void runActionForCard(GameController gameController, Card myCard, Card opponentCard){
        setBoards(gameController, myCard);
        if(opponentCard == null){
            switch(myCard.getName()){
                case "Man-Eater Bug":
                    ManEaterBug manEaterBug = new ManEaterBug();
                    manEaterBug.runFirstAction(gameController, myCard, null);
                    break;
                case "Scanner":
                    Scanner scanner = new Scanner();
                    scanner.runFirstAction(gameController, myCard, null);
                    break;
                case "Beast King Barbaros":
                    BeastKingBarbaros beastKingBarbaros = new BeastKingBarbaros();
                    beastKingBarbaros.runFirstAction(gameController, myCard, null);
                    break;
                case "Texchanger":
                    Texchanger texchanger = new Texchanger();
                    texchanger.runFirstAction(gameController, myCard, null);
                    break;
                case "The Calculator":
                    TheCalculator theCalculator = new TheCalculator();
                    theCalculator.runFirstAction(gameController, myCard, null);
                    break;
                case "Mirage Dragon":
                    MirageDragon mirageDragon = new MirageDragon();
                    mirageDragon.runFirstAction(gameController, myCard, null);
                    break;
                case "Herald of Creation":
                    HeraldOfCreation heraldOfCreation = new HeraldOfCreation();
                    heraldOfCreation.runFirstAction(gameController, myCard, null);
                    break;
                case "Terratiger, the Empowered Warrior":
                    Terratiger terratiger = new Terratiger();
                    terratiger.runFirstAction(gameController, myCard, null);
                    break;
                case "The Tricky":
                    TheTricky theTricky = new TheTricky();
                    theTricky.runFirstAction(gameController, myCard, null);
                    break;
                case "Command Knight":
                    CommandKnight commandKnight = new CommandKnight();
                    commandKnight.runFirstAction(gameController, myCard, null);
                    break;


                case "Trap Hole":
                    TrapHole trapHole = new TrapHole();
                    trapHole.runFirstAction(gameController, myCard, null);
                    break;
                case "Mirror Force":
                    MirrorForce mirrorForce = new MirrorForce();
                    mirrorForce.runFirstAction(gameController, myCard, null);
                    break;
                case "Mind Crush":
                    MindCrush mindCrush = new MindCrush();
                    mindCrush.runFirstAction(gameController, myCard, null);
                    break;
                case "Torrential Tribute":
                    TorrentialTribute torrentialTribute = new TorrentialTribute();
                    torrentialTribute.runFirstAction(gameController, myCard, null);
                    break;
                case "Time Seal":
                    TimeSeal timeSeal = new TimeSeal();
                    timeSeal.runFirstAction(gameController, myCard, null);
                    break;
                case "Call of The Haunted":
                    CallOfTheHaunted callOfTheHaunted = new CallOfTheHaunted();
                    callOfTheHaunted.runFirstAction(gameController, myCard, null);
                    break;
                case "Monster Reborn":
                    MonsterReborn monsterReborn = new MonsterReborn();
                    monsterReborn.runFirstAction(gameController, myCard, null);
                    break;
                case "Terraforming":
                    Terraforming terraforming = new Terraforming();
                    terraforming.runFirstAction(gameController, myCard, null);
                    break;
                case "Pot of Greed":
                    PotOfGreed potOfGreed = new PotOfGreed();
                    potOfGreed.runFirstAction(gameController, myCard, null);
                    break;
                case "Raigeki":
                    Raigeki raigeki = new Raigeki();
                    raigeki.runFirstAction(gameController, myCard, null);
                    break;
                case "Change of Heart":
                    ChangeOfHeart changeOfHeart = new ChangeOfHeart();
                    changeOfHeart.runFirstAction(gameController, myCard, null);
                    break;
                case "Swords of Revealing Light":
                    SwordsOfRevealingLight swordsOfRevealingLight = new SwordsOfRevealingLight();
                    swordsOfRevealingLight.runFirstAction(gameController, myCard, null);
                    break;
                case "Harpie's Feather Duster":
                    HarpiesFeatherDuster harpiesFeatherDuster = new HarpiesFeatherDuster();
                    harpiesFeatherDuster.runFirstAction(gameController, myCard, null);
                    break;
                case "Dark Hole":
                    DarkHole darkHole = new DarkHole();
                    darkHole.runFirstAction(gameController, myCard, null);
                    break;
                case "Supply Squad":
                    SupplySquad supplySquad = new SupplySquad();
                    supplySquad.runFirstAction(gameController, myCard, null);
                    break;
                case "Spell Absorption":
                    SpellAbsorption spellAbsorption = new SpellAbsorption();
                    spellAbsorption.runFirstAction(gameController, myCard, null);
                    break;
                case "Messenger of peace":
                    MessengerOfPeace messengerOfPeace = new MessengerOfPeace();
                    messengerOfPeace.runFirstAction(gameController, myCard, null);
                    break;
                case "Twin Twisters":
                    TwinTwisters twinTwisters = new TwinTwisters();
                    twinTwisters.runFirstAction(gameController, myCard, null);
                    break;
                case "Mystical space typhoon":
                    MysticalSpaceTyphoon mysticalSpaceTyphoon = new MysticalSpaceTyphoon();
                    mysticalSpaceTyphoon.runFirstAction(gameController, myCard, null);
                    break;
                case "Advanced Ritual Art":
                    AdvancedRitualArt advancedRitualArt = new AdvancedRitualArt();
                    advancedRitualArt.runFirstAction(gameController, myCard, null);
                    break;

                case "Yami":
                case "Forest":
                case "Closed Forest":
                case "Umiiruka":
                    FieldSpell fieldSpell = new FieldSpell();
                    fieldSpell.runFirstAction(gameController, myCard, null);
                    break;
                case "Sword of dark destruction":
                case "Black Pendant":
                case "United We Stand":
                case "Magnum Shield":
                    EquipSpell equipSpell = new EquipSpell();
                    equipSpell.runFirstAction(gameController, myCard, null);
                    break;

                default:
                    break;
            }
        } else {
            switch(myCard.getName()){
                case "Yomi Ship":
                    YomiShip yomiShip = new YomiShip();
                    yomiShip.runActionForDefense(gameController, myCard, opponentCard);
                    break;
                case "Suijin":
                    Suijin suijin = new Suijin();
                    suijin.runActionForDefense(gameController, myCard, opponentCard);
                    break;
                case "Marshmallon":
                    Marshmallon marshmallon = new Marshmallon();
                    marshmallon.runActionForDefense(gameController, myCard, opponentCard);
                    break;
                case "Texchanger":
                    Texchanger texchanger = new Texchanger();
                    texchanger.runActionForDefense(gameController, myCard, opponentCard);
                    break;
                case "Exploder Dragon":
                    ExploderDragon exploderDragon = new ExploderDragon();
                    exploderDragon.runActionForDefense(gameController, myCard, opponentCard);
                    break;
                case "Command Knight":
                    CommandKnight commandKnight = new CommandKnight();
                    commandKnight.runActionForDefense(gameController, myCard, opponentCard);
                    break;


                case "Trap Hole":
                    TrapHole trapHole = new TrapHole();
                    trapHole.runActionForDefense(gameController, myCard, opponentCard);
                    break;
                case "Mind Crush":
                    MindCrush mindCrush = new MindCrush();
                    mindCrush.runActionForDefense(gameController, myCard, opponentCard);
                    break;
                case "Mirror Force":
                    MirrorForce mirrorForce = new MirrorForce();
                    mirrorForce.runActionForDefense(gameController, myCard, opponentCard);
                    break;
                case "Magic Cylinder":
                    MagicCylinder magicCylinder = new MagicCylinder();
                    magicCylinder.runActionForDefense(gameController, myCard, opponentCard);
                    break;
                case "Torrential Tribute":
                    TorrentialTribute torrentialTribute = new TorrentialTribute();
                    torrentialTribute.runActionForDefense(gameController, myCard, opponentCard);
                    break;
                case "Time Seal":
                    TimeSeal timeSeal = new TimeSeal();
                    timeSeal.runActionForDefense(gameController, myCard, opponentCard);
                    break;
                case "Negate Attack":
                    NegateAttack negateAttack = new NegateAttack();
                    negateAttack.runActionForDefense(gameController, myCard, opponentCard);
                    break;
                case "Solemn Warning":
                    SolemnWarning solemnWarning = new SolemnWarning();
                    solemnWarning.runActionForDefense(gameController, myCard, opponentCard);
                    break;
                case "Magic Jammer":
                    MagicJammer magicJammer = new MagicJammer();
                    magicJammer.runActionForDefense(gameController, myCard, opponentCard);
                    break;
                case "Call of The Haunted":
                    CallOfTheHaunted callOfTheHaunted = new CallOfTheHaunted();
                    callOfTheHaunted.runActionForDefense(gameController, myCard, opponentCard);
                    break;
                case "Monster Reborn":
                    MonsterReborn monsterReborn = new MonsterReborn();
                    monsterReborn.runActionForDefense(gameController, myCard, opponentCard);
                    break;
                case "Terraforming":
                    Terraforming terraforming = new Terraforming();
                    terraforming.runActionForDefense(gameController, myCard, opponentCard);
                    break;
                case "Pot of Greed":
                    PotOfGreed potOfGreed = new PotOfGreed();
                    potOfGreed.runActionForDefense(gameController, myCard, opponentCard);
                    break;
                case "Raigeki":
                    Raigeki raigeki = new Raigeki();
                    raigeki.runActionForDefense(gameController, myCard, opponentCard);
                    break;
                case "Harpie's Feather Duster":
                    HarpiesFeatherDuster harpiesFeatherDuster = new HarpiesFeatherDuster();
                    harpiesFeatherDuster.runActionForDefense(gameController, myCard, opponentCard);
                    break;
                case "Dark Hole":
                    DarkHole darkHole = new DarkHole();
                    darkHole.runActionForDefense(gameController, myCard, opponentCard);
                    break;
                case "Supply Squad":
                    SupplySquad supplySquad = new SupplySquad();
                    supplySquad.runActionForDefense(gameController, myCard, opponentCard);
                    break;
                case "Spell Absorption":
                    SpellAbsorption spellAbsorption = new SpellAbsorption();
                    spellAbsorption.runActionForDefense(gameController, myCard, opponentCard);
                    break;
                case "Messenger of peace":
                    MessengerOfPeace messengerOfPeace = new MessengerOfPeace();
                    messengerOfPeace.runActionForDefense(gameController, myCard, opponentCard);
                    break;
                case "Twin Twisters":
                    TwinTwisters twinTwisters = new TwinTwisters();
                    twinTwisters.runActionForDefense(gameController, myCard, opponentCard);
                    break;
                case "Mystical space typhoon":
                    MysticalSpaceTyphoon mysticalSpaceTyphoon = new MysticalSpaceTyphoon();
                    mysticalSpaceTyphoon.runActionForDefense(gameController, myCard, opponentCard);
                    break;
                case "Ring of defense":
                    RingOfDefense ringOfDefense = new RingOfDefense();
                    ringOfDefense.runActionForDefense(gameController, myCard, opponentCard);
                    break;
                default:
                    break;
            }
        }
    }

    public static boolean canEffectBeActivatedForCard(GameController gameController, Card myCard, Card opponentCard){
        setBoards(gameController, myCard);
        if(myCard.isTrap() && !myBoard.canAnyTrapBeActivated()) return false;
        switch(myCard.getName()){
            case "Man-Eater Bug":
                ManEaterBug manEaterBug = new ManEaterBug();
                return manEaterBug.canEffectBeActivated(gameController, myCard, opponentCard);
            case "Scanner":
                Scanner scanner = new Scanner();
                return scanner.canEffectBeActivated(gameController, myCard, opponentCard);
            case "Beast King Barbaros":
                BeastKingBarbaros beastKingBarbaros = new BeastKingBarbaros();
                return beastKingBarbaros.canEffectBeActivated(gameController, myCard, opponentCard);
            case "The Calculator":
                TheCalculator theCalculator = new TheCalculator();
                return theCalculator.canEffectBeActivated(gameController, myCard, opponentCard);
            case "Mirage Dragon":
                MirageDragon mirageDragon = new MirageDragon();
                return mirageDragon.canEffectBeActivated(gameController, myCard, opponentCard);
            case "Herald of Creation":
                HeraldOfCreation heraldOfCreation = new HeraldOfCreation();
                return heraldOfCreation.canEffectBeActivated(gameController, myCard, opponentCard);
            case "Terratiger, the Empowered Warrior":
                Terratiger terratiger = new Terratiger();
                return terratiger.canEffectBeActivated(gameController, myCard, opponentCard);
            case "The Tricky":
                TheTricky theTricky = new TheTricky();
                return theTricky.canEffectBeActivated(gameController, myCard, opponentCard);
            case "Yomi Ship":
                YomiShip yomiShip = new YomiShip();
                return yomiShip.canEffectBeActivated(gameController, myCard, opponentCard);
            case "Suijin":
                Suijin suijin = new Suijin();
                return suijin.canEffectBeActivated(gameController, myCard, opponentCard);
            case "Marshmallon":
                Marshmallon marshmallon = new Marshmallon();
                return marshmallon.canEffectBeActivated(gameController, myCard, opponentCard);
            case "Texchanger":
                Texchanger texchanger = new Texchanger();
                return texchanger.canEffectBeActivated(gameController, myCard, opponentCard);
            case "Exploder Dragon":
                ExploderDragon exploderDragon = new ExploderDragon();
                return exploderDragon.canEffectBeActivated(gameController, myCard, opponentCard);
            case "Command Knight":
                CommandKnight commandKnight = new CommandKnight();
                return commandKnight.canEffectBeActivated(gameController, myCard, opponentCard);


            case "Trap Hole":
                TrapHole trapHole = new TrapHole();
                return trapHole.canEffectBeActivated(gameController, myCard, opponentCard);
            case "Mind Crush":
                MindCrush mindCrush = new MindCrush();
                return mindCrush.canEffectBeActivated(gameController, myCard, opponentCard);
            case "Mirror Force":
                MirrorForce mirrorForce = new MirrorForce();
                return mirrorForce.canEffectBeActivated(gameController, myCard, opponentCard);
            case "Magic Cylinder":
                MagicCylinder magicCylinder = new MagicCylinder();
                return magicCylinder.canEffectBeActivated(gameController, myCard, opponentCard);
            case "Torrential Tribute":
                TorrentialTribute torrentialTribute = new TorrentialTribute();
                return torrentialTribute.canEffectBeActivated(gameController, myCard, opponentCard);
            case "Time Seal":
                TimeSeal timeSeal = new TimeSeal();
                return timeSeal.canEffectBeActivated(gameController, myCard, opponentCard);
            case "Negate Attack":
                NegateAttack negateAttack = new NegateAttack();
                return negateAttack.canEffectBeActivated(gameController, myCard, opponentCard);
            case "Solemn Warning":
                SolemnWarning solemnWarning = new SolemnWarning();
                return solemnWarning.canEffectBeActivated(gameController, myCard, opponentCard);
            case "Magic Jammer":
                MagicJammer magicJammer = new MagicJammer();
                return magicJammer.canEffectBeActivated(gameController, myCard, opponentCard);
            case "Call of The Haunted":
                CallOfTheHaunted callOfTheHaunted = new CallOfTheHaunted();
                return callOfTheHaunted.canEffectBeActivated(gameController, myCard, opponentCard);
            case "Monster Reborn":
                MonsterReborn monsterReborn = new MonsterReborn();
                return monsterReborn.canEffectBeActivated(gameController, myCard, opponentCard);
            case "Terraforming":
                Terraforming terraforming = new Terraforming();
                return terraforming.canEffectBeActivated(gameController, myCard, opponentCard);
            case "Pot of Greed":
                PotOfGreed potOfGreed = new PotOfGreed();
                return potOfGreed.canEffectBeActivated(gameController, myCard, opponentCard);
            case "Raigeki":
                Raigeki raigeki = new Raigeki();
                return raigeki.canEffectBeActivated(gameController, myCard, opponentCard);
            case "Harpie's Feather Duster":
                HarpiesFeatherDuster harpiesFeatherDuster = new HarpiesFeatherDuster();
                return harpiesFeatherDuster.canEffectBeActivated(gameController, myCard, opponentCard);
            case "Dark Hole":
                DarkHole darkHole = new DarkHole();
                return darkHole.canEffectBeActivated(gameController, myCard, opponentCard);
            case "Supply Squad":
                SupplySquad supplySquad = new SupplySquad();
                return supplySquad.canEffectBeActivated(gameController, myCard, opponentCard);
            case "Spell Absorption":
                SpellAbsorption spellAbsorption = new SpellAbsorption();
                return spellAbsorption.canEffectBeActivated(gameController, myCard, opponentCard);
            case "Messenger of peace":
                MessengerOfPeace messengerOfPeace = new MessengerOfPeace();
                return messengerOfPeace.canEffectBeActivated(gameController, myCard, opponentCard);
            case "Twin Twisters":
                TwinTwisters twinTwisters = new TwinTwisters();
                return twinTwisters.canEffectBeActivated(gameController, myCard, opponentCard);
            case "Mystical space typhoon":
                MysticalSpaceTyphoon mysticalSpaceTyphoon = new MysticalSpaceTyphoon();
                return mysticalSpaceTyphoon.canEffectBeActivated(gameController, myCard, opponentCard);
            case "Ring of defense":
                RingOfDefense ringOfDefense = new RingOfDefense();
                return ringOfDefense.canEffectBeActivated(gameController, myCard, opponentCard);
            case "Change of Heart":
                ChangeOfHeart changeOfHeart = new ChangeOfHeart();
                return changeOfHeart.canEffectBeActivated(gameController, myCard, opponentCard);
            case "Swords of Revealing Light":
                SwordsOfRevealingLight swordsOfRevealingLight = new SwordsOfRevealingLight();
                return swordsOfRevealingLight.canEffectBeActivated(gameController, myCard, opponentCard);

            case "Yami":
            case "Forest":
            case "Closed Forest":
            case "Umiiruka":
                FieldSpell fieldSpell = new FieldSpell();
                return fieldSpell.canEffectBeActivated(gameController, myCard, opponentCard);
            case "Sword of dark destruction":
            case "Black Pendant":
            case "United We Stand":
            case "Magnum Shield":
                EquipSpell equipSpell = new EquipSpell();
                return equipSpell.canEffectBeActivated(gameController, myCard, opponentCard);

            default:
                return false;
        }
    }
}

