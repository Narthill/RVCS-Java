package com.narthil.rvcs.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.narthil.rvcs.dao.member.MemberDao;
import com.narthil.rvcs.dao.member.MemberRepository;
import com.narthil.rvcs.dto.ResultInfo;
import com.narthil.rvcs.pojo.MemberInfo;
import com.narthil.rvcs.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * MemberServiceImpl
 */
@Service
public class MemberServiceImpl implements MemberService{

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private MemberRepository memberRepository;

    // 添加成员
    public  ResultInfo<Map<String,Object>> addMember(MemberInfo member,String userId) {
        ResultInfo<Map<String,Object>> memResult=new ResultInfo<Map<String,Object>>();
        // 判断权限,以当前操作者的根组织和其id进行判断
        if(member!=null){
            int auth=memberDao.getAuthNum(member.getRootId(),userId);
            if(auth!=1&&auth!=2){
                memResult.setStatus(401,"权限不足");
                return memResult;
            }
            // 判断待添加的用户是否已经存在在该公司
            if(memberDao.findByUserIdAndRootId(member.getRootId(),member.getUserId())==null){
                member.setAuth(0);
                member.setAuthDesc("默认权限");
                MemberInfo mTemp=memberRepository.save(member);
        
                Map<String,Object> dataMap=new HashMap<String,Object>(){
                    private static final long serialVersionUID = -5985072152836372366L;

                    {
                        // put("id",mTemp.getId());
                        put("userId", mTemp.getUserId());
                        put("rootId", mTemp.getRootId());
                        put("orgId", mTemp.getOrgId());
                        put("auth",mTemp.getAuth());
                        put("authDesc",mTemp.getAuthDesc());
                    }
                };
                memResult.setData(dataMap);
                memResult.setStatus(201,"添加成员成功");
            }else{
                memResult.setStatus(400,"此成员已经在该公司中");
            }

        }else{
            memResult.setStatus(400,"添加成员失败");
        }
        
        return memResult;
    }


    // 删除成员
    // public ResultInfo<Object> deleteMember(MemberInfo member,String userId){
    //     ResultInfo<Object> memResult=new ResultInfo<Object>();
    //     if(member!=null){
    //         int auth=memberDao.getAuthNum(member.getRootId(),userId);
    //         if(auth!=1&&auth!=2){
    //             memResult.setStatus(401,"权限不足");
    //             return memResult;
    //         }
    //         // 验证此人是否在公司
    //         if(memberDao.findByUserIdAndRootId(member.getRootId(),member.getUserId())!=null){

    //         }

    //     return null;
    // }

    // 修改成员权限
    public ResultInfo<Object> updateAuth(String rootId,String userId,String adminId,int auth){
        return null;
    }
}