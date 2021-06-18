package Models;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface ICheatCode {
    public Pattern increaseHealthPattern = Pattern.compile("^increasemylp(\\d+)$");
    public Pattern increaseMoneyPattern = Pattern.compile("^increasemymoney(\\d+)$");
    public Pattern duelSetWinnerPattern = Pattern.compile("^setwinner ([^ ]+)$");
    public Pattern selectExtraCardPattern = Pattern.compile("^selecthand (.+) force$");

    public void increaseLP(Matcher matcher);
    public void increaseMoney(Matcher matcher);

}
