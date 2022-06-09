package game.utils.sensitive;

/**
 * <p>
 * 匹配到的单词，包含单词，text中匹配单词的内容，以及匹配内容在text中的下标，
 * 下标可以用来做单词的进一步处理，如果替换成**
 */
public class FoundWord {

	/**
	 * 起始位置（包含）
	 */
	private int startIndex;

	/**
	 * 结束位置（包含）
	 */
	private int endIndex;

	/**
	 * 生效的单词，即单词树中的词
	 */
	private final String word;
	/**
	 * 单词匹配到的内容，即文中的单词
	 */
	private final String foundWord;

	public FoundWord(String word, String foundWord, int startIndex, int endIndex) {
		this.word = word;
		this.foundWord = foundWord;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public String getWord() {
		return word;
	}

	public String getFoundWord() {
		return foundWord;
	}

	/**
	 * 默认的，只输出匹配到的关键字
	 * @return 匹配到的关键字
	 */
	@Override
	public String toString() {
		return this.foundWord;
	}
}
