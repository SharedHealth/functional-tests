function BaseClass() { }
BaseClass.prototype.getName = function() {
    return "BaseClass(" + this.getId() + ")";
}

BaseClass.prototype.getId = function() {
    return 1;
}

function SubClass() {}
SubClass.prototype = new BaseClass();
SubClass.prototype.getName = function() {
    //A little 'apply' magic and polymorphism works!
    //But ugh, explicit references!
    return "SubClass(" + this.getId() + ") extends " +
        BaseClass.prototype.getName.call(this);
}

SubClass.prototype.getId = function() {
    return 2;
}

//Alerts "SubClass(2) extends BaseClass(2)";
//Hey, we got the proper output!
console.log(new SubClass().getName());