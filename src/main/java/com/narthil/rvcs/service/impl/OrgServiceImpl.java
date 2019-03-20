package com.narthil.rvcs.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.narthil.rvcs.dao.member.MemberDao;
import com.narthil.rvcs.dao.org.OrgDao;
import com.narthil.rvcs.dao.member.MemberRepository;
import com.narthil.rvcs.dao.org.OrgRepository;
import com.narthil.rvcs.dto.ResultInfo;
import com.narthil.rvcs.pojo.MemberInfo;
import com.narthil.rvcs.pojo.OrgInfo;
import com.narthil.rvcs.service.OrgService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * OrgServiceImpl
 */
@Service
public class OrgServiceImpl implements OrgService{

    @Autowired
    private OrgDao orgDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrgRepository orgRepository;
    @Autowired
    private MemberRepository memberRepository;

    // 创建公司
    @Override
    public ResultInfo<Map<String,Object>> createCompany(OrgInfo company,String userId){

        ResultInfo<Map<String,Object>> orgResult=new ResultInfo<Map<String,Object>>();
        if(company!=null){
            //节点表插入数据，返回
            OrgInfo comTemp=orgRepository.save(company);
            // 构造成员信息
            MemberInfo member=new MemberInfo(){
                {
                    setUserId(userId);
                    setRootId(comTemp.getId());
                    setOrgId(comTemp.getId());
                    setAuth(1);
                    setAuthDesc("主管理员");
                }
            };
            // 在成员表中添加对应信息
            MemberInfo memberTemp=memberRepository.save(member);

            //构建返回结果 
            Map<String,Object> dataMap=new HashMap<String,Object>(){
                {
                    put("id",comTemp.getId());
                    put("root",comTemp.getRoot());
                    put("name",comTemp.getName());
                    put("desc",comTemp.getDesc());
                    put("members",memberTemp);
                }
            };

            orgResult.setData(dataMap);
            orgResult.setStatus(1,"添加公司信息成功");
        }else{
            orgResult.setStatus(0,"添加公司信息失败");
        }
        return orgResult;
    }

    // 创建节点
    public ResultInfo<Map<String,Object>> createOrg(OrgInfo node,String userId){

        ResultInfo<Map<String,Object>> orgResult=new ResultInfo<Map<String,Object>>();
        String parent=node.getParent();
        // 判断权限
        int auth=memberDao.getAuthNum(node.getRootId(),userId);
        System.out.println(auth);
        if(auth!=1&&auth!=2){
            orgResult.setStatus(0,"权限不足");
            return orgResult;
        }

        // 获取插入对象
        OrgInfo orgTemp = orgRepository.insert(node);
        // 编写返回信息
        Map<String, Object> dataMap = new HashMap<String, Object>() {
            {
                put("id", orgTemp.getId());
                put("root", orgTemp.getRoot());
                put("rootId", orgTemp.getRootId());
                put("name", orgTemp.getName());
                put("desc", orgTemp.getDesc());
                put("parent", orgTemp.getParent());
            }
        };
        String childId = orgTemp.getId();

        if (orgDao.insertChildNode(parent, childId)) {
            orgResult.setData(dataMap);
            orgResult.setStatus(1, "添加组织节点成功");
        } else {
            // 应当删除
            orgResult.setStatus(0, "父节点添加组织节点失败");
        }
        
        return orgResult;
    }

    // 查询节点以及其人员信息
    public ResultInfo<Map<String,Object>> findOrgAndMember(String orgId){
        ResultInfo<Map<String,Object>> orgResult=new ResultInfo<Map<String,Object>>();

        if(orgId!=null){
            OrgInfo orgInfo=orgRepository.findById(orgId);
            List<MemberInfo> memberTable= memberRepository.findByOrgId(orgId);

            Map<String,Object> dataMap=new HashMap<String,Object>(){
                {
                    put("orgInfo",orgInfo);
                    put("members",memberTable);
                }
            };
            orgResult.setData(dataMap);
            orgResult.setStatus(1,"查询成功");

        }else{
            orgResult.setStatus(0,"查询失败");
        }
        return orgResult;
    }

    // 查询当前用户的所在公司信息
    public ResultInfo<Set<OrgInfo>> findComInfo(String userId){
        ResultInfo<Set<OrgInfo>> orgResult=new ResultInfo<Set<OrgInfo>>();

        if(userId!=null){
            // 找到当前用户的所有成员信息表
           List<MemberInfo> userOrgList=memberRepository.findByUserId(userId);
            // 从成员信息表中获取所有的公司id
            if(userOrgList!=null){
                Set<OrgInfo> comList=new HashSet<OrgInfo>();
                for(MemberInfo tmp:userOrgList){
                     comList.add( orgDao.findComInfo(tmp.getRootId()));
                }
                orgResult.setData(comList);
                orgResult.setStatus(1,"查询成功");
            }else{
                orgResult.setStatus(0,"该用户未加入任何组织");
            }
        }else{
            orgResult.setStatus(0,"查询失败");
        }
        return orgResult;
    }
}