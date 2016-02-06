package se.alten.swearc.webserver.dummy;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public interface RandomWords {

	public static String get() {
		Random random = new java.util.Random();
		int index = random.nextInt(words.size());
		return words.get(index);
	}

	final static List<String> words = Arrays.asList(
			"Everyday a grape licks a friendly cow.", "Look, a distraction!",
			"A hotdog on a bridge.", "I am a purple dinosaur called steve!",
			"This is the single best thing online.", "Message appoved!",
			"Purple, because playground eggs wear torn scarf hats.",
			"None, because snakes don't have armpits.", "Banana error.",
			"A bugger glances a capital.", "The instructed libel fasts.",
			"The continuum despairs throughout an understanding prefix.",
			"The consulting misprint presses a minimum palace.",
			"A naughty refund hopes the mania.",
			"The flipping keystroke defects.", "The entrance reigns.",
			"The dash rash changes below the mythology.",
			"The voter recruits the central litter.",
			"The motivating handicap collapses.", "A scholar stops!",
			"A curly chairman teams the hack etymology.",
			"The sermon dictates with the farmer.",
			"The refreshing vintage enters with the gift.",
			"The concept waits around the scientist.",
			"The calm adopts a rapid essence.",
			"An undocumented pencil invests its latter cobbler.", "able",
			"above", "across", "add", "against", "ago", "almost", "among",
			"animal", "answer", "became", "become", "began", "behind", "being",
			"better", "black", "best", "body", "book", "boy", "brought",
			"call", "cannot", "car", "certain", "change", "children", "city",
			"close", "cold", "country", "course", "cut", "didn't", "dog",
			"done", "door", "draw", "during", "early", "earth", "eat",
			"enough", "ever", "example", "eye", "face", "family", "far",
			"father", "feel", "feet", "fire", "fish", "five", "food", "form",
			"four", "front", "gave", "given", "got", "green", "ground",
			"group", "grow", "half", "hand", "hard", "heard", "high",
			"himself", "however", "I'll", "I'm", "idea", "important", "inside",
			"John", "keep", "kind", "knew", "known", "land", "later", "learn",
			"let", "letter", "life", "light", "live", "living", "making",
			"mean", "means", "money", "morning", "mother", "move", "Mrs.",
			"near", "night", "nothing", "once", "open", "order", "page",
			"paper", "parts", "perhaps", "picture", "play", "point", "ready",
			"red", "remember", "rest", "room", "run", "school", "sea",
			"second", "seen", "sentence", "several", "short", "shown", "since",
			"six", "slide", "sometime", "soon", "space", "States", "story",
			"sun", "sure", "table", "though", "today", "told", "took", "top",
			"toward", "tree", "try", "turn", "United", "until", "upon",
			"using", "usually", "white", "whole", "wind", "without", "yes",
			"yet", "young", "spoon", "bottle cap", "nail clippers", "candle",
			"ice cube", "slipper", "thread", "glow stick", "needle",
			"stop sign", "hanger", "rubber duck", "shovel", "bookmark",
			"model car", "rubber band", "tire swing", "sharpie",
			"picture frame", "photo album", "nail filer", "tooth paste",
			"deodorant", "cookie jar", "rusty nail", "drill press", "chalk",
			"word search", "thermometer", "face wash", "paint brush",
			"candy wrapper", "shoe lace", "leg warmers", "wireless control",
			"boom box", "quilt", "about", "after", "again", "air", "all",
			"along", "also", "an", "and", "another", "any", "are", "around",
			"as", "at", "away", "back", "be", "because", "been", "before",
			"below", "between", "both", "but", "by", "came", "can", "come",
			"could", "day", "did", "different", "do", "does", "don't", "down",
			"each", "end", "even", "every", "few", "find", "first", "for",
			"found", "from", "get", "give", "go", "good", "great", "had",
			"has", "have", "he", "help", "her", "here", "him", "his", "home",
			"house", "how", "I", "if", "in", "into", "is", "it", "its", "just",
			"know", "large", "last", "left", "like", "line", "little", "long",
			"look", "made", "make", "man", "many", "may", "me", "men", "might",
			"more", "most", "Mr.", "must", "my", "name", "never", "new",
			"next", "no", "not", "now", "number", "of", "off", "old", "on",
			"one", "only", "or", "other", "our", "out", "over", "own", "part",
			"people", "place", "put", "read", "right", "said", "same", "saw",
			"say", "see", "she", "should", "show", "small", "so", "some",
			"something", "sound", "still", "such", "take", "tell", "than",
			"that", "the", "them", "then", "there", "these", "they", "thing",
			"think", "this", "those", "thought", "three", "through", "time",
			"to", "together", "too", "two", "under", "up", "us", "use", "very",
			"want", "water", "way", "we", "well", "went", "were", "what",
			"when", "where", "which", "while", "who", "why", "will", "with",
			"word", "work", "world", "would", "write", "year", "you", "your",
			"was");
}
