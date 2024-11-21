/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mundo;

/**
 *
 * @author j8318
 */
public abstract class FinancialDocument {
    public abstract String generateDocument();
    protected abstract void header();
    protected abstract void body();
    protected abstract void footer();
    public abstract double calculateTotal();
}
