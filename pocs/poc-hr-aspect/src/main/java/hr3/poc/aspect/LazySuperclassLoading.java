package hr3.poc.aspect;

public interface LazySuperclassLoading {
	// superclass already loaded
	boolean isLoaded();

	void loadSuperclass();

	void unloadSuperclass();
}
