#Description
PopperFramework is a java framework allowing you to create PageObjects for your surface tests in a declarative way. This means you just have to define an interface defining the structure of your website and add some annotation with the basic information like locators. PopperFramework will generate the implementation of that PageObject for you.

PopperFramework supports Page Objects for Web via WebDriver-API and Swing via Jemmy.

#Example
``
@Page
public interface LoginSite {
  @Locator(xpath = "//input[@name='username']")
  ITextBox usernameTextbox();

  @Locator(xpath = "//input[@name='password']")
  ITextBox passwordTextbox();

  @Locator(cssSelector = ".submitLoginButton")
  IButton submitLoginButton();

  @Locator(id = "register")
  ILink registerLink();
}
``
