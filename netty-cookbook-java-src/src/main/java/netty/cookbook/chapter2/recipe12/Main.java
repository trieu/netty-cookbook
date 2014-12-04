package netty.cookbook.chapter2.recipe12;

import netty.cookbook.chapter2.recipe12.MyModel.Person;

public class Main {

	public static void main(String[] args) {
		MyModel model = new MyModel();
		MyObserver observer = new MyObserver(model);
		// we change the last name of the person, observer will get notified
		for (Person person : model.getPersons()) {
			person.setLastName(person.getLastName() + "1");
		}
		// we change the name of the person, observer will get notified
		for (Person person : model.getPersons()) {
			person.setFirstName(person.getFirstName() + "1");
		}
	}
}
