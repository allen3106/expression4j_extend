在我们的项目中，如果是做流程或者是一些涉及到计算的模块时，经常有动态进行计算的需求，或者是动态的编写一个计算公式进行计算的业务场景；比如：一个计算有关三角函数的业务模板 sin(A) + cos(B)；如果是你单独去实现的话；虽然不难但是还要要花一番时间的；今天在这里给大家推荐一款这样的开源项目，expression4j专门就是来进行这样的动态公式计算的。原项目地址：github.com/gohutool/expression4j   最新版本0.03版已经好久没有更新了。

扩展expression4j增加一些方法，一些运算符。对function的参数要求进行了改动，允许可变参数（参数个数可以变化），比如说：sum(x,y,z...),min(x,y,z...),max(x,y,z...)
扩展方法有：if,and,or,not chose,match,avg,eq，mod.....
扩展的运算符：常用的逻辑运算符
 Operator gt = OperatorFactory.createOperator("gt", ">", false);
Operator ge = OperatorFactory.createOperator("ge", ">=", false);
Operator lt = OperatorFactory.createOperator("lt", "<", false);
Operator le = OperatorFactory.createOperator("le", "<=", false);
Operator eq = OperatorFactory.createOperator("eq", "==", false);
Operator ne = OperatorFactory.createOperator("ne", "!=", false);
Operator not = OperatorFactory.createOperator("not", "!", true);
Operator mod = OperatorFactory.createOperator("mod", "%", false);
Operator or = OperatorFactory.createOperator("or", "||", false);
Operator and = OperatorFactory.createOperator("and", "&&", false);

新增的代码，90%的代码都在Expression4jUtil 里面，对原有部分类就行了小修改，随便修改，随便传播。

