// Generated from /Users/jpanda/work/codes/catman/catman-jsonpath/catman-jsonpath/src/main/resources/ObjectPath.g4 by ANTLR 4.13.1
package cc.catman.object.path.standard;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ObjectPathParser}.
 */
public interface ObjectPathListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ObjectPathParser#objectPath}.
	 * @param ctx the parse tree
	 */
	void enterObjectPath(ObjectPathParser.ObjectPathContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObjectPathParser#objectPath}.
	 * @param ctx the parse tree
	 */
	void exitObjectPath(ObjectPathParser.ObjectPathContext ctx);
	/**
	 * Enter a parse tree produced by the {@code COMPLEX_VALUE_EXPR}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCOMPLEX_VALUE_EXPR(ObjectPathParser.COMPLEX_VALUE_EXPRContext ctx);
	/**
	 * Exit a parse tree produced by the {@code COMPLEX_VALUE_EXPR}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCOMPLEX_VALUE_EXPR(ObjectPathParser.COMPLEX_VALUE_EXPRContext ctx);
	/**
	 * Enter a parse tree produced by the {@code EXPR_FUNC_EXPR}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterEXPR_FUNC_EXPR(ObjectPathParser.EXPR_FUNC_EXPRContext ctx);
	/**
	 * Exit a parse tree produced by the {@code EXPR_FUNC_EXPR}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitEXPR_FUNC_EXPR(ObjectPathParser.EXPR_FUNC_EXPRContext ctx);
	/**
	 * Enter a parse tree produced by the {@code GROUP_EXPR}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterGROUP_EXPR(ObjectPathParser.GROUP_EXPRContext ctx);
	/**
	 * Exit a parse tree produced by the {@code GROUP_EXPR}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitGROUP_EXPR(ObjectPathParser.GROUP_EXPRContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CALCULATE_EXPR}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCALCULATE_EXPR(ObjectPathParser.CALCULATE_EXPRContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CALCULATE_EXPR}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCALCULATE_EXPR(ObjectPathParser.CALCULATE_EXPRContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OR}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterOR(ObjectPathParser.ORContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OR}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitOR(ObjectPathParser.ORContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RREGEX}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterRREGEX(ObjectPathParser.RREGEXContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RREGEX}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitRREGEX(ObjectPathParser.RREGEXContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LT}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLT(ObjectPathParser.LTContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LT}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLT(ObjectPathParser.LTContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DEFAULT_EXPR}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterDEFAULT_EXPR(ObjectPathParser.DEFAULT_EXPRContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DEFAULT_EXPR}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitDEFAULT_EXPR(ObjectPathParser.DEFAULT_EXPRContext ctx);
	/**
	 * Enter a parse tree produced by the {@code EQ}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterEQ(ObjectPathParser.EQContext ctx);
	/**
	 * Exit a parse tree produced by the {@code EQ}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitEQ(ObjectPathParser.EQContext ctx);
	/**
	 * Enter a parse tree produced by the {@code GT}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterGT(ObjectPathParser.GTContext ctx);
	/**
	 * Exit a parse tree produced by the {@code GT}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitGT(ObjectPathParser.GTContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LREGEX}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLREGEX(ObjectPathParser.LREGEXContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LREGEX}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLREGEX(ObjectPathParser.LREGEXContext ctx);
	/**
	 * Enter a parse tree produced by the {@code REG_EXPR}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterREG_EXPR(ObjectPathParser.REG_EXPRContext ctx);
	/**
	 * Exit a parse tree produced by the {@code REG_EXPR}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitREG_EXPR(ObjectPathParser.REG_EXPRContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VALUE_EXPR}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterVALUE_EXPR(ObjectPathParser.VALUE_EXPRContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VALUE_EXPR}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitVALUE_EXPR(ObjectPathParser.VALUE_EXPRContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LIKE}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLIKE(ObjectPathParser.LIKEContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LIKE}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLIKE(ObjectPathParser.LIKEContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AND}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAND(ObjectPathParser.ANDContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AND}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAND(ObjectPathParser.ANDContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TYPE_CAST_EXPR}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterTYPE_CAST_EXPR(ObjectPathParser.TYPE_CAST_EXPRContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TYPE_CAST_EXPR}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitTYPE_CAST_EXPR(ObjectPathParser.TYPE_CAST_EXPRContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LE}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLE(ObjectPathParser.LEContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LE}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLE(ObjectPathParser.LEContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ISNULL}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterISNULL(ObjectPathParser.ISNULLContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ISNULL}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitISNULL(ObjectPathParser.ISNULLContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NEQ}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterNEQ(ObjectPathParser.NEQContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NEQ}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitNEQ(ObjectPathParser.NEQContext ctx);
	/**
	 * Enter a parse tree produced by the {@code REVERT_EXPR}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterREVERT_EXPR(ObjectPathParser.REVERT_EXPRContext ctx);
	/**
	 * Exit a parse tree produced by the {@code REVERT_EXPR}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitREVERT_EXPR(ObjectPathParser.REVERT_EXPRContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PATH_EXPR}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterPATH_EXPR(ObjectPathParser.PATH_EXPRContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PATH_EXPR}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitPATH_EXPR(ObjectPathParser.PATH_EXPRContext ctx);
	/**
	 * Enter a parse tree produced by the {@code GE}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterGE(ObjectPathParser.GEContext ctx);
	/**
	 * Exit a parse tree produced by the {@code GE}
	 * labeled alternative in {@link ObjectPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitGE(ObjectPathParser.GEContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ROOT_NODE}
	 * labeled alternative in {@link ObjectPathParser#location}.
	 * @param ctx the parse tree
	 */
	void enterROOT_NODE(ObjectPathParser.ROOT_NODEContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ROOT_NODE}
	 * labeled alternative in {@link ObjectPathParser#location}.
	 * @param ctx the parse tree
	 */
	void exitROOT_NODE(ObjectPathParser.ROOT_NODEContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CURRENT_NODE}
	 * labeled alternative in {@link ObjectPathParser#location}.
	 * @param ctx the parse tree
	 */
	void enterCURRENT_NODE(ObjectPathParser.CURRENT_NODEContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CURRENT_NODE}
	 * labeled alternative in {@link ObjectPathParser#location}.
	 * @param ctx the parse tree
	 */
	void exitCURRENT_NODE(ObjectPathParser.CURRENT_NODEContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PARENT_NODE}
	 * labeled alternative in {@link ObjectPathParser#location}.
	 * @param ctx the parse tree
	 */
	void enterPARENT_NODE(ObjectPathParser.PARENT_NODEContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PARENT_NODE}
	 * labeled alternative in {@link ObjectPathParser#location}.
	 * @param ctx the parse tree
	 */
	void exitPARENT_NODE(ObjectPathParser.PARENT_NODEContext ctx);
	/**
	 * Enter a parse tree produced by the {@code METHOD}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 */
	void enterMETHOD(ObjectPathParser.METHODContext ctx);
	/**
	 * Exit a parse tree produced by the {@code METHOD}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 */
	void exitMETHOD(ObjectPathParser.METHODContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CHILD}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 */
	void enterCHILD(ObjectPathParser.CHILDContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CHILD}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 */
	void exitCHILD(ObjectPathParser.CHILDContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RECURSIVE_CHILD}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 */
	void enterRECURSIVE_CHILD(ObjectPathParser.RECURSIVE_CHILDContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RECURSIVE_CHILD}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 */
	void exitRECURSIVE_CHILD(ObjectPathParser.RECURSIVE_CHILDContext ctx);
	/**
	 * Enter a parse tree produced by the {@code INDEX_OR_NAME}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 */
	void enterINDEX_OR_NAME(ObjectPathParser.INDEX_OR_NAMEContext ctx);
	/**
	 * Exit a parse tree produced by the {@code INDEX_OR_NAME}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 */
	void exitINDEX_OR_NAME(ObjectPathParser.INDEX_OR_NAMEContext ctx);
	/**
	 * Enter a parse tree produced by the {@code INDEX_OR_NAME_LIST}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 */
	void enterINDEX_OR_NAME_LIST(ObjectPathParser.INDEX_OR_NAME_LISTContext ctx);
	/**
	 * Exit a parse tree produced by the {@code INDEX_OR_NAME_LIST}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 */
	void exitINDEX_OR_NAME_LIST(ObjectPathParser.INDEX_OR_NAME_LISTContext ctx);
	/**
	 * Enter a parse tree produced by the {@code INDEX}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 */
	void enterINDEX(ObjectPathParser.INDEXContext ctx);
	/**
	 * Exit a parse tree produced by the {@code INDEX}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 */
	void exitINDEX(ObjectPathParser.INDEXContext ctx);
	/**
	 * Enter a parse tree produced by the {@code WILDCARD_ALL}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 */
	void enterWILDCARD_ALL(ObjectPathParser.WILDCARD_ALLContext ctx);
	/**
	 * Exit a parse tree produced by the {@code WILDCARD_ALL}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 */
	void exitWILDCARD_ALL(ObjectPathParser.WILDCARD_ALLContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SLICE}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 */
	void enterSLICE(ObjectPathParser.SLICEContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SLICE}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 */
	void exitSLICE(ObjectPathParser.SLICEContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SLICE_PICK}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 */
	void enterSLICE_PICK(ObjectPathParser.SLICE_PICKContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SLICE_PICK}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 */
	void exitSLICE_PICK(ObjectPathParser.SLICE_PICKContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FILTER}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 */
	void enterFILTER(ObjectPathParser.FILTERContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FILTER}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 */
	void exitFILTER(ObjectPathParser.FILTERContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SCRIPT}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 */
	void enterSCRIPT(ObjectPathParser.SCRIPTContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SCRIPT}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 */
	void exitSCRIPT(ObjectPathParser.SCRIPTContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UP}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 */
	void enterUP(ObjectPathParser.UPContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UP}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 */
	void exitUP(ObjectPathParser.UPContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ALL}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 */
	void enterALL(ObjectPathParser.ALLContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ALL}
	 * labeled alternative in {@link ObjectPathParser#selector}.
	 * @param ctx the parse tree
	 */
	void exitALL(ObjectPathParser.ALLContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObjectPathParser#pipe}.
	 * @param ctx the parse tree
	 */
	void enterPipe(ObjectPathParser.PipeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObjectPathParser#pipe}.
	 * @param ctx the parse tree
	 */
	void exitPipe(ObjectPathParser.PipeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObjectPathParser#typeCast}.
	 * @param ctx the parse tree
	 */
	void enterTypeCast(ObjectPathParser.TypeCastContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObjectPathParser#typeCast}.
	 * @param ctx the parse tree
	 */
	void exitTypeCast(ObjectPathParser.TypeCastContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObjectPathParser#filterExpr}.
	 * @param ctx the parse tree
	 */
	void enterFilterExpr(ObjectPathParser.FilterExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObjectPathParser#filterExpr}.
	 * @param ctx the parse tree
	 */
	void exitFilterExpr(ObjectPathParser.FilterExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MIN}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterMIN(ObjectPathParser.MINContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MIN}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitMIN(ObjectPathParser.MINContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MAX}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterMAX(ObjectPathParser.MAXContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MAX}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitMAX(ObjectPathParser.MAXContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SUM}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterSUM(ObjectPathParser.SUMContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SUM}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitSUM(ObjectPathParser.SUMContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AVG}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterAVG(ObjectPathParser.AVGContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AVG}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitAVG(ObjectPathParser.AVGContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SIZE}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterSIZE(ObjectPathParser.SIZEContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SIZE}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitSIZE(ObjectPathParser.SIZEContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ISEMPTY}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterISEMPTY(ObjectPathParser.ISEMPTYContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ISEMPTY}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitISEMPTY(ObjectPathParser.ISEMPTYContext ctx);
	/**
	 * Enter a parse tree produced by the {@code METHOD_INDEX}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterMETHOD_INDEX(ObjectPathParser.METHOD_INDEXContext ctx);
	/**
	 * Exit a parse tree produced by the {@code METHOD_INDEX}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitMETHOD_INDEX(ObjectPathParser.METHOD_INDEXContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LENGTH}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterLENGTH(ObjectPathParser.LENGTHContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LENGTH}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitLENGTH(ObjectPathParser.LENGTHContext ctx);
	/**
	 * Enter a parse tree produced by the {@code KEYS}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterKEYS(ObjectPathParser.KEYSContext ctx);
	/**
	 * Exit a parse tree produced by the {@code KEYS}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitKEYS(ObjectPathParser.KEYSContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VALUES}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterVALUES(ObjectPathParser.VALUESContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VALUES}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitVALUES(ObjectPathParser.VALUESContext ctx);
	/**
	 * Enter a parse tree produced by the {@code REVERSE}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterREVERSE(ObjectPathParser.REVERSEContext ctx);
	/**
	 * Exit a parse tree produced by the {@code REVERSE}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitREVERSE(ObjectPathParser.REVERSEContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DISTINCT}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterDISTINCT(ObjectPathParser.DISTINCTContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DISTINCT}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitDISTINCT(ObjectPathParser.DISTINCTContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SORT}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterSORT(ObjectPathParser.SORTContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SORT}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitSORT(ObjectPathParser.SORTContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RSORT}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterRSORT(ObjectPathParser.RSORTContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RSORT}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitRSORT(ObjectPathParser.RSORTContext ctx);
	/**
	 * Enter a parse tree produced by the {@code GROUPBY}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterGROUPBY(ObjectPathParser.GROUPBYContext ctx);
	/**
	 * Exit a parse tree produced by the {@code GROUPBY}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitGROUPBY(ObjectPathParser.GROUPBYContext ctx);
	/**
	 * Enter a parse tree produced by the {@code JOIN}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterJOIN(ObjectPathParser.JOINContext ctx);
	/**
	 * Exit a parse tree produced by the {@code JOIN}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitJOIN(ObjectPathParser.JOINContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SPLIT}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterSPLIT(ObjectPathParser.SPLITContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SPLIT}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitSPLIT(ObjectPathParser.SPLITContext ctx);
	/**
	 * Enter a parse tree produced by the {@code REPLACE}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterREPLACE(ObjectPathParser.REPLACEContext ctx);
	/**
	 * Exit a parse tree produced by the {@code REPLACE}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitREPLACE(ObjectPathParser.REPLACEContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SUBSTRING}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterSUBSTRING(ObjectPathParser.SUBSTRINGContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SUBSTRING}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitSUBSTRING(ObjectPathParser.SUBSTRINGContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TOUPPER}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterTOUPPER(ObjectPathParser.TOUPPERContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TOUPPER}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitTOUPPER(ObjectPathParser.TOUPPERContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TOLOWER}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterTOLOWER(ObjectPathParser.TOLOWERContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TOLOWER}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitTOLOWER(ObjectPathParser.TOLOWERContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TRIM}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterTRIM(ObjectPathParser.TRIMContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TRIM}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitTRIM(ObjectPathParser.TRIMContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LTRIM}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterLTRIM(ObjectPathParser.LTRIMContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LTRIM}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitLTRIM(ObjectPathParser.LTRIMContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RTRIM}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterRTRIM(ObjectPathParser.RTRIMContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RTRIM}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitRTRIM(ObjectPathParser.RTRIMContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CONCAT}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterCONCAT(ObjectPathParser.CONCATContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CONCAT}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitCONCAT(ObjectPathParser.CONCATContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FIRST}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterFIRST(ObjectPathParser.FIRSTContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FIRST}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitFIRST(ObjectPathParser.FIRSTContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LAST}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterLAST(ObjectPathParser.LASTContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LAST}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitLAST(ObjectPathParser.LASTContext ctx);
	/**
	 * Enter a parse tree produced by the {@code INDEXOF}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterINDEXOF(ObjectPathParser.INDEXOFContext ctx);
	/**
	 * Exit a parse tree produced by the {@code INDEXOF}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitINDEXOF(ObjectPathParser.INDEXOFContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MAP}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterMAP(ObjectPathParser.MAPContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MAP}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitMAP(ObjectPathParser.MAPContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TOMAP_METHOD}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterTOMAP_METHOD(ObjectPathParser.TOMAP_METHODContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TOMAP_METHOD}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitTOMAP_METHOD(ObjectPathParser.TOMAP_METHODContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IS_NULL_METHOD}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterIS_NULL_METHOD(ObjectPathParser.IS_NULL_METHODContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IS_NULL_METHOD}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitIS_NULL_METHOD(ObjectPathParser.IS_NULL_METHODContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NOT_NULL_METHOD}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterNOT_NULL_METHOD(ObjectPathParser.NOT_NULL_METHODContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NOT_NULL_METHOD}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitNOT_NULL_METHOD(ObjectPathParser.NOT_NULL_METHODContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AND_METHOD}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterAND_METHOD(ObjectPathParser.AND_METHODContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AND_METHOD}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitAND_METHOD(ObjectPathParser.AND_METHODContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OR_METHOD}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterOR_METHOD(ObjectPathParser.OR_METHODContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OR_METHOD}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitOR_METHOD(ObjectPathParser.OR_METHODContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NOT_METHOD}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterNOT_METHOD(ObjectPathParser.NOT_METHODContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NOT_METHOD}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitNOT_METHOD(ObjectPathParser.NOT_METHODContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FILTER_METHOD}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterFILTER_METHOD(ObjectPathParser.FILTER_METHODContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FILTER_METHOD}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitFILTER_METHOD(ObjectPathParser.FILTER_METHODContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BETWEEN}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterBETWEEN(ObjectPathParser.BETWEENContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BETWEEN}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitBETWEEN(ObjectPathParser.BETWEENContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LIKE_METHOD}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterLIKE_METHOD(ObjectPathParser.LIKE_METHODContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LIKE_METHOD}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitLIKE_METHOD(ObjectPathParser.LIKE_METHODContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IN}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterIN(ObjectPathParser.INContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IN}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitIN(ObjectPathParser.INContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NOTIN}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterNOTIN(ObjectPathParser.NOTINContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NOTIN}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitNOTIN(ObjectPathParser.NOTINContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CONTAINS}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterCONTAINS(ObjectPathParser.CONTAINSContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CONTAINS}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitCONTAINS(ObjectPathParser.CONTAINSContext ctx);
	/**
	 * Enter a parse tree produced by the {@code STARTSWITH}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterSTARTSWITH(ObjectPathParser.STARTSWITHContext ctx);
	/**
	 * Exit a parse tree produced by the {@code STARTSWITH}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitSTARTSWITH(ObjectPathParser.STARTSWITHContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ENDSWITH}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterENDSWITH(ObjectPathParser.ENDSWITHContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ENDSWITH}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitENDSWITH(ObjectPathParser.ENDSWITHContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MATCHES}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterMATCHES(ObjectPathParser.MATCHESContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MATCHES}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitMATCHES(ObjectPathParser.MATCHESContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RAW_METHOD}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterRAW_METHOD(ObjectPathParser.RAW_METHODContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RAW_METHOD}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitRAW_METHOD(ObjectPathParser.RAW_METHODContext ctx);
	/**
	 * Enter a parse tree produced by the {@code METHOD_CALL}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterMETHOD_CALL(ObjectPathParser.METHOD_CALLContext ctx);
	/**
	 * Exit a parse tree produced by the {@code METHOD_CALL}
	 * labeled alternative in {@link ObjectPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitMETHOD_CALL(ObjectPathParser.METHOD_CALLContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObjectPathParser#scripts}.
	 * @param ctx the parse tree
	 */
	void enterScripts(ObjectPathParser.ScriptsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObjectPathParser#scripts}.
	 * @param ctx the parse tree
	 */
	void exitScripts(ObjectPathParser.ScriptsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObjectPathParser#scripts_start}.
	 * @param ctx the parse tree
	 */
	void enterScripts_start(ObjectPathParser.Scripts_startContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObjectPathParser#scripts_start}.
	 * @param ctx the parse tree
	 */
	void exitScripts_start(ObjectPathParser.Scripts_startContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObjectPathParser#scripts_end}.
	 * @param ctx the parse tree
	 */
	void enterScripts_end(ObjectPathParser.Scripts_endContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObjectPathParser#scripts_end}.
	 * @param ctx the parse tree
	 */
	void exitScripts_end(ObjectPathParser.Scripts_endContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObjectPathParser#namedArgs}.
	 * @param ctx the parse tree
	 */
	void enterNamedArgs(ObjectPathParser.NamedArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObjectPathParser#namedArgs}.
	 * @param ctx the parse tree
	 */
	void exitNamedArgs(ObjectPathParser.NamedArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObjectPathParser#namedArg}.
	 * @param ctx the parse tree
	 */
	void enterNamedArg(ObjectPathParser.NamedArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObjectPathParser#namedArg}.
	 * @param ctx the parse tree
	 */
	void exitNamedArg(ObjectPathParser.NamedArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObjectPathParser#args}.
	 * @param ctx the parse tree
	 */
	void enterArgs(ObjectPathParser.ArgsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObjectPathParser#args}.
	 * @param ctx the parse tree
	 */
	void exitArgs(ObjectPathParser.ArgsContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObjectPathParser#arg}.
	 * @param ctx the parse tree
	 */
	void enterArg(ObjectPathParser.ArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObjectPathParser#arg}.
	 * @param ctx the parse tree
	 */
	void exitArg(ObjectPathParser.ArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObjectPathParser#complexValue}.
	 * @param ctx the parse tree
	 */
	void enterComplexValue(ObjectPathParser.ComplexValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObjectPathParser#complexValue}.
	 * @param ctx the parse tree
	 */
	void exitComplexValue(ObjectPathParser.ComplexValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObjectPathParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(ObjectPathParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObjectPathParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(ObjectPathParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObjectPathParser#json}.
	 * @param ctx the parse tree
	 */
	void enterJson(ObjectPathParser.JsonContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObjectPathParser#json}.
	 * @param ctx the parse tree
	 */
	void exitJson(ObjectPathParser.JsonContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObjectPathParser#object}.
	 * @param ctx the parse tree
	 */
	void enterObject(ObjectPathParser.ObjectContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObjectPathParser#object}.
	 * @param ctx the parse tree
	 */
	void exitObject(ObjectPathParser.ObjectContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObjectPathParser#array}.
	 * @param ctx the parse tree
	 */
	void enterArray(ObjectPathParser.ArrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObjectPathParser#array}.
	 * @param ctx the parse tree
	 */
	void exitArray(ObjectPathParser.ArrayContext ctx);
	/**
	 * Enter a parse tree produced by {@link ObjectPathParser#pair}.
	 * @param ctx the parse tree
	 */
	void enterPair(ObjectPathParser.PairContext ctx);
	/**
	 * Exit a parse tree produced by {@link ObjectPathParser#pair}.
	 * @param ctx the parse tree
	 */
	void exitPair(ObjectPathParser.PairContext ctx);
}