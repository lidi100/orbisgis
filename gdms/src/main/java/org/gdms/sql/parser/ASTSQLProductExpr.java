/* Generated By:JJTree: Do not edit this line. ASTSQLProductExpr.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package org.gdms.sql.parser;

public
class ASTSQLProductExpr extends SimpleNode {
  public ASTSQLProductExpr(int id) {
    super(id);
  }

  public ASTSQLProductExpr(SQLEngine p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SQLEngineVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=8781473b66275fbcb56b354808570944 (do not edit this line) */