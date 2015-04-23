function Mammal() {};
Mammal.prototype.legs = function ()
{
    return "Has legs";
}

function Human() {};

Human.prototype = new Mammal();
Human.prototype.Talk = function() {
    return "Can Talk";
};

me = new Human();

console.log(me.legs());
console.log(me.Talk());




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
    //Call the getName() method of the super class
    //Hmm... is an explicit reference really a super call?
    return "SubClass(" + this.getId() + ") extends " +
        BaseClass.prototype.getName();
}

SubClass.prototype.getId = function() {
    return 2;
}

//Alerts "SubClass(2) extends BaseClass(1)";
//Is this the proper output?
console.log(new SubClass().getName());