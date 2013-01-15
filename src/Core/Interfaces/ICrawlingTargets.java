package Core.Interfaces;

public interface ICrawlingTargets {
	String GetNextTarget();
	boolean AddTarget(String sTarget);
	long NumbertOfTargets();
}
